package kvraft

const (
	OK             = "OK"
	ErrNoKey       = "ErrNoKey"
	ErrWrongLeader = "ErrWrongLeader"
)

type Err string

// Put or Append
type PutAppendArgs struct {
	Key   string
	Value string
	ClerkId int64
	TaskId int64
}

type PutAppendReply struct {
	Err Err
}

type GetArgs struct {
	Key string
	ClerkId int64
	TaskId int64
}

type GetReply struct {
	Err   Err
	Value string
}
