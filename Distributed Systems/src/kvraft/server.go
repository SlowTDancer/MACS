package kvraft

import (
	"6.5840/labgob"
	"6.5840/labrpc"
	"6.5840/raft"
	"log"
	"sync"
	"sync/atomic"
	"time"
	"bytes"
)

const Debug = false

const (
	GET 	= "GET"
	PUT     = "PUT"
	APPEND 	= "APPEND"
)

func DPrintf(format string, a ...interface{}) (n int, err error) {
	if Debug {
		log.Printf(format, a...)
	}
	return
}


type Op struct {
	Type	string
	Key 	string
	Value	string
	ClerkId	int64
	TaskId	int64
}

type KVServer struct {
	mu      sync.Mutex
	me      int
	rf      *raft.Raft
	applyCh chan raft.ApplyMsg
	dead    int32 // set by Kill()

	maxraftstate int // snapshot if log grows this big
	persister *raft.Persister

	mp				map[string]string
	lastAppliedMap	map[int64]int64
	chanMap			map[int]chan Op
}


func (kv *KVServer) Get(args *GetArgs, reply *GetReply) {
	key := args.Key
	taskId := args.TaskId
	clerkId := args.ClerkId

	op := Op{
		Type:		GET,
		Key:		key,
		Value:		"",
		ClerkId: 	clerkId,
		TaskId: 	taskId,
	}

	isLeader := kv.raftStart(op)

	if !isLeader {
		reply.Err = ErrWrongLeader
		return
	}

	kv.mu.Lock()
	defer kv.mu.Unlock()

	val, ok := kv.mp[key]

	if ok {
		reply.Value = val
		reply.Err = OK
	} else {
		reply.Value = ""
		reply.Err = ErrNoKey
	}
}

func (kv *KVServer) Put(args *PutAppendArgs, reply *PutAppendReply) {
	key := args.Key
    value := args.Value
	taskId := args.TaskId
	clerkId := args.ClerkId

	op := Op{
		Type:		PUT,
		Key:		key,
		Value:		value,
		ClerkId: 	clerkId,
		TaskId: 	taskId,
	}

	isLeader := kv.raftStart(op)
	
	if !isLeader {
		reply.Err = ErrWrongLeader
	} else {
		reply.Err = OK
	}
}


func (kv *KVServer) Append(args *PutAppendArgs, reply *PutAppendReply) {
	key := args.Key
    value := args.Value
	taskId := args.TaskId
	clerkId := args.ClerkId

	op := Op{
		Type:		APPEND,
		Key:		key,
		Value:		value,
		ClerkId: 	clerkId,
		TaskId: 	taskId,
	}

	isLeader := kv.raftStart(op)

	if !isLeader {
		reply.Err = ErrWrongLeader
	} else {
		reply.Err = OK
	}
}


func (kv *KVServer) raftStart(op Op) bool {
	kv.mu.Lock()
	
	index, _, isLeader := kv.rf.Start(op)
	
	if !isLeader {
		kv.mu.Unlock()
		return false
	}
	
	ch := make(chan Op, 1)
	kv.chanMap[index] = ch
	defer kv.chanDelete(index)
	kv.mu.Unlock()

	select {
	case operation := <-ch:
		if operation.ClerkId == op.ClerkId && operation.TaskId == op.TaskId {
			return true
		} else {
			return false
		}
	case <-time.After(500 * time.Millisecond):
		return false
	}
}	


func (kv *KVServer) applyCommand(op Op) {
	kv.mu.Lock()
	defer kv.mu.Unlock()

	if lastApplied, ok := kv.lastAppliedMap[op.ClerkId]; ok && lastApplied >= op.TaskId {
		return
	}

	switch op.Type {
	case PUT:
		kv.mp[op.Key] = op.Value
	case APPEND:
		value, ok := kv.mp[op.Key]
		if ok {
			kv.mp[op.Key] = value + op.Value
		} else {
			kv.mp[op.Key] = op.Value
		}
	}

	kv.lastAppliedMap[op.ClerkId] = op.TaskId
}


func (kv *KVServer) chanDelete(index int) {
	kv.mu.Lock()
	defer kv.mu.Unlock()
	delete(kv.chanMap, index)
}


func (kv *KVServer) chanBroadcast(index int, op Op) {
	kv.mu.Lock()
	defer kv.mu.Unlock()
	if ch, ok := kv.chanMap[index]; ok {
		ch <- op
	}
}


func (kv *KVServer) checkSnapshot(index int) {
	if kv.maxraftstate <= 0 || kv.persister.RaftStateSize() < kv.maxraftstate {
		return
	}
	go kv.snapshot(index)
}


func (kv *KVServer) applier() {
	for !kv.killed() {
		msg := <-kv.applyCh
		if msg.CommandValid {
			op := msg.Command.(Op)
			kv.applyCommand(op)
			kv.chanBroadcast(msg.CommandIndex, op)
			kv.checkSnapshot(msg.CommandIndex)
		} else if msg.SnapshotValid {
			kv.readSnapshot(msg.Snapshot)
		}
	}
}


func (kv *KVServer) readSnapshot(snapshot []byte) {
	if snapshot == nil || len(snapshot) < 1 {
		return
	}
	r := bytes.NewBuffer(snapshot)
	d := labgob.NewDecoder(r)

	var mp map[string]string
	var lastAppliedMap map[int64]int64

	if d.Decode(&mp) != nil || d.Decode(&lastAppliedMap) != nil {
		panic("Error decoding snapshot")
	}

	kv.mu.Lock()
	defer kv.mu.Unlock()

	kv.mp = mp
	kv.lastAppliedMap = lastAppliedMap
}


func (kv *KVServer) snapshot(index int) {
	kv.mu.Lock()
	defer kv.mu.Unlock()

	w := new(bytes.Buffer)
	e := labgob.NewEncoder(w)

	e.Encode(kv.mp)
	e.Encode(kv.lastAppliedMap)

	ss := w.Bytes()
	kv.rf.Snapshot(index, ss)
}


// the tester calls Kill() when a KVServer instance won't
// be needed again. for your convenience, we supply
// code to set rf.dead (without needing a lock),
// and a killed() method to test rf.dead in
// long-running loops. you can also add your own
// code to Kill(). you're not required to do anything
// about this, but it may be convenient (for example)
// to suppress debug output from a Kill()ed instance.
func (kv *KVServer) Kill() {
	atomic.StoreInt32(&kv.dead, 1)
	kv.rf.Kill()
	// Your code here, if desired.
}


func (kv *KVServer) killed() bool {
	z := atomic.LoadInt32(&kv.dead)
	return z == 1
}


// servers[] contains the ports of the set of
// servers that will cooperate via Raft to
// form the fault-tolerant key/value service.
// me is the index of the current server in servers[].
// the k/v server should store snapshots through the underlying Raft
// implementation, which should call persister.SaveStateAndSnapshot() to
// atomically save the Raft state along with the snapshot.
// the k/v server should snapshot when Raft's saved state exceeds maxraftstate bytes,
// in order to allow Raft to garbage-collect its log. if maxraftstate is -1,
// you don't need to snapshot.
// StartKVServer() must return quickly, so it should start goroutines
// for any long-running work.
func StartKVServer(servers []*labrpc.ClientEnd, me int, persister *raft.Persister, maxraftstate int) *KVServer {
	// call labgob.Register on structures you want
	// Go's RPC library to marshall/unmarshall.
	labgob.Register(Op{})

	kv := new(KVServer)
	kv.me = me
	kv.maxraftstate = maxraftstate
	kv.persister = persister

	kv.applyCh = make(chan raft.ApplyMsg)
	kv.rf = raft.Make(servers, me, persister, kv.applyCh)

	kv.mp = make(map[string]string)
	kv.lastAppliedMap = make(map[int64]int64)
	kv.chanMap = make(map[int]chan Op)

	snapshot := persister.ReadSnapshot()
	kv.readSnapshot(snapshot)

	go kv.applier()

	return kv
}
