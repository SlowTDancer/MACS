package kvraft

import "6.5840/labrpc"
import "crypto/rand"
import "math/big"
// import "log"


type Clerk struct {
	servers []*labrpc.ClientEnd

	clerkId 	int64
	taskId 		int64
	lastLeader	int
}

func nrand() int64 {
	max := big.NewInt(int64(1) << 62)
	bigx, _ := rand.Int(rand.Reader, max)
	x := bigx.Int64()
	return x
}

func MakeClerk(servers []*labrpc.ClientEnd) *Clerk {
	ck := new(Clerk)
	
	ck.servers = servers
	ck.clerkId = nrand()
	ck.taskId = 0
	ck.lastLeader = 0

	return ck
}

func addMod(a int, b int, mod int) int {
	return (a + b) % mod;
} 

// fetch the current value for a key.
// returns "" if the key does not exist.
// keeps trying forever in the face of all other errors.
//
// you can send an RPC with code like this:
// ok := ck.servers[i].Call("KVServer."+op, &args, &reply)
//
// the types of args and reply (including whether they are pointers)
// must match the declared types of the RPC handler function's
// arguments. and reply must be passed as a pointer.
func (ck *Clerk) Get(key string) string {
	args := GetArgs{Key: key, ClerkId: ck.clerkId, TaskId: ck.taskId}
	
	for i := ck.lastLeader;  ; i = addMod(i, 1, len(ck.servers)) {
		reply := GetReply{}
		ok := ck.servers[i].Call("KVServer.Get", &args, &reply)

		if ok && reply.Err != ErrWrongLeader {
			ck.lastLeader = i
			ck.taskId++
			return reply.Value
		}
	}

}

// shared by Put and Append.
//
// you can send an RPC with code like this:
// ok := ck.servers[i].Call("KVServer.PutAppend", &args, &reply)
//
// the types of args and reply (including whether they are pointers)
// must match the declared types of the RPC handler function's
// arguments. and reply must be passed as a pointer.
func (ck *Clerk) PutAppend(key string, value string, op string) {
	args := PutAppendArgs{Key: key, Value: value, ClerkId: ck.clerkId, TaskId: ck.taskId}

	for i := ck.lastLeader;  ; i = addMod(i, 1, len(ck.servers)) {
		reply := PutAppendReply{}	
		ok := ck.servers[i].Call("KVServer." + op, &args, &reply)
		
		if ok && reply.Err != ErrWrongLeader {
			ck.lastLeader = i
			ck.taskId++
			return
		}
	}
}

func (ck *Clerk) Put(key string, value string) {
	ck.PutAppend(key, value, "Put")
}
func (ck *Clerk) Append(key string, value string) {
	ck.PutAppend(key, value, "Append")
}
