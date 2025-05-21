package kvsrv

import (
	"log"
	"sync"
)

const Debug = false

func DPrintf(format string, a ...interface{}) (n int, err error) {
	if Debug {
		log.Printf(format, a...)
	}
	return
}

type Data struct{
	TaskId int64
	ReplyValue string
}

type KVServer struct {
	mu sync.Mutex
	mp map[string]string
	clerkMap map[int64]Data
}


func (kv *KVServer) Get(args *GetArgs, reply *GetReply) {
	kv.mu.Lock()
	defer kv.mu.Unlock()

	key := args.Key
	elem, ok := kv.mp[key]

	if ok{
		reply.Value = elem
	}else{
		reply.Value = ""
	}
}

func (kv *KVServer) Put(args *PutAppendArgs, reply *PutAppendReply) {
	kv.mu.Lock()
	defer kv.mu.Unlock()

	key := args.Key
    value := args.Value
	
	kv.mp[key] = value
}

func (kv *KVServer) Append(args *PutAppendArgs, reply *PutAppendReply) {
	kv.mu.Lock()
	defer kv.mu.Unlock()

	clerkId := args.ClerkId
	taskId := args.TaskId

	clerkData, okClerk := kv.clerkMap[clerkId]

	if okClerk && clerkData.TaskId == taskId {
		reply.Value = kv.clerkMap[clerkId].ReplyValue
		return
	}
	
	key := args.Key
	value := args.Value
	elem, ok := kv.mp[key]
	if ok{
		reply.Value = elem
		kv.mp[key] = elem + value
	}else{
		reply.Value = ""
		kv.mp[key] = value
	}

	kv.clerkMap[clerkId] = Data{TaskId: taskId, ReplyValue: reply.Value}
}

func StartKVServer() *KVServer {
	kv := new(KVServer)

	kv.mp = make(map[string]string)
	kv.clerkMap = make(map[int64]Data)

	return kv
}
