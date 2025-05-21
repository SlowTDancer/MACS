package raft

//
// this is an outline of the API that raft must expose to
// the service (or tester). see comments below for
// each of these functions for more details.
//
// rf = Make(...)
//   create a new Raft server.
// rf.Start(command interface{}) (index, term, isleader)
//   start agreement on a new log entry
// rf.GetState() (term, isLeader)
//   ask a Raft for its current term, and whether it thinks it is leader
// ApplyMsg
//   each time a new entry is committed to the log, each Raft peer
//   should send an ApplyMsg to the service (or tester)
//   in the same server.
//

import (
	"bytes"
	"math/rand"
	"sync"
	"sync/atomic"
	"time"
	"sort"
	"log"

	"6.5840/labgob"
	"6.5840/labrpc"
)


const (
	FOLLOWER 			= 0
	CANDIDATE 			= 1
	LEADER 				= 2

	BEAT_TIME 			= 150
	MIN_ELEC_TIMEOUT 	= 300
	ELEC_TIMEOUT_SIZE 	= 300
)


///////UTILS////////
func (rf *Raft) PrintMI() {
    log.Printf("[DEBUG] me: %d, l: %d, term: %d, rf.matchIndex entries:", rf.me, rf.serverState, rf.currentTerm)
    for i, entry := range rf.matchIndex {
        log.Printf("Index: %d, Term: %d, ci: %d", i, entry, rf.commitIndex)
    }
}

func min(a int, b int) int {
	if a > b {
		return b
	}
	return a
}

func max(a int, b int) int {
	if a > b {
		return a
	}
	return b
}

func (rf *Raft) PrintLog() {
    log.Printf("[DEBUG] me: %d, l: %d, term: %d, rf.log entries:", rf.me, rf.serverState, rf.currentTerm)
    for i, entry := range rf.log {
        log.Printf("Index: %d, Term: %d, Command: %v", i, entry.Term, entry.Command)
    }
}

func (rf *Raft) getLog(index int) Log {
	if index < rf.logOffset {
        // log.Printf("[ERROR] Attempting to access index %d below logOffset %d", index, rf.logOffset)
        return Log{}
    }
	return rf.log[index - rf.logOffset]
}

func insert(arr []Log, index int, value Log) []Log {
    arr = append(arr, Log{})
    copy(arr[index + 1:], arr[index:])
    arr[index] = value
    return arr
}
///////UTILS////////

// as each Raft peer becomes aware that successive log entries are
// committed, the peer should send an ApplyMsg to the service (or
// tester) on the same server, via the applyCh passed to Make(). set
// CommandValid to true to indicate that the ApplyMsg contains a newly
// committed log entry.
//
// in part 3D you'll want to send other kinds of messages (e.g.,
// snapshots) on the applyCh, but set CommandValid to false for these
// other uses.
type ApplyMsg struct {
	CommandValid bool
	Command      interface{}
	CommandIndex int

	// For 3D:
	SnapshotValid bool
	Snapshot      []byte
	SnapshotTerm  int
	SnapshotIndex int
}

type Log struct {
	Term 			int
	Command      	interface{}
}

// A Go object implementing a single Raft peer.
type Raft struct {
	mu        		sync.Mutex          // Lock to protect shared access to this peer's state
	peers     		[]*labrpc.ClientEnd // RPC end points of all peers
	persister 		*Persister          // Object to hold this peer's persisted state
	me        		int                 // this peer's index into peers[]
	dead      		int32               // set by Kill()

	// 	Persistent state on all servers:
	// (Updated on stable storage before responding to RPCs)
	currentTerm 	int
	votedFor		int
	log 			[]Log

	// Volatile state on all servers:
	commitIndex 	int
	lastApplied 	int

	// Volatile state on leaders:
	// (Reinitialized after election)
	nextIndex	 	[]int
	matchIndex		[]int

	serverState		int
	voteCount		int
	lastBeat		time.Time
	applyCh 		chan ApplyMsg
	timeout			time.Duration

	logOffset		int
	lastIncludedTerm 	int
	snapshot		[]byte
	// Your data here (3A, 3B, 3C).
	// Look at the paper's Figure 2 for a description of what
	// state a Raft server must maintain.
}

// return currentTerm and whether this server
// believes it is the leader.
func (rf *Raft) GetState() (int, bool) {
	var term int
	var isleader bool
	
	rf.mu.Lock()
	defer rf.mu.Unlock()

	term = rf.currentTerm
	isleader = (rf.serverState == LEADER)
	// log.Printf("[getState] term: %d, isLeader: %v", rf.currentTerm, isleader)
	return term, isleader
}

// save Raft's persistent state to stable storage,
// where it can later be retrieved after a crash and restart.
// see paper's Figure 2 for a description of what should be persistent.
// before you've implemented snapshots, you should pass nil as the
// second argument to persister.Save().
// after you've implemented snapshots, pass the current snapshot
// (or nil if there's not yet a snapshot).
func (rf *Raft) persist() {
	// Your code here (3C).
	// Example:
	w := new(bytes.Buffer)
	e := labgob.NewEncoder(w)
	e.Encode(rf.currentTerm)
	e.Encode(rf.votedFor)
	e.Encode(rf.log)
	e.Encode(rf.logOffset)
	e.Encode(rf.lastIncludedTerm)
	raftstate := w.Bytes()
	rf.persister.Save(raftstate, rf.snapshot)
}

// restore previously persisted state.
func (rf *Raft) readPersist(data []byte) {
	if data == nil || len(data) < 1 { // bootstrap without any state?
		return
	}
	// Your code here (3C).
	// Example:
	r := bytes.NewBuffer(data)
	d := labgob.NewDecoder(r)
	var currentTerm int
	var votedFor int
	var myLog []Log
	var logOffset int
	var lastIncludedTerm int

	if d.Decode(&currentTerm) != nil ||
	   d.Decode(&votedFor) != nil || 
	   d.Decode(&myLog) != nil || 
	   d.Decode(&logOffset) != nil ||
	   d.Decode(&lastIncludedTerm) != nil {
		log.Printf("[READPERSIST] damerxa")
	} else {
	  	rf.currentTerm = currentTerm
	  	rf.votedFor = votedFor
	  	rf.log = myLog	
		rf.logOffset = logOffset
		rf.lastIncludedTerm = lastIncludedTerm
		rf.snapshot = rf.persister.ReadSnapshot()
	}
}


// the service says it has created a snapshot that has
// all info up to and including index. this means the
// service no longer needs the log through (and including)
// that index. Raft should now trim its log as much as possible.
func (rf *Raft) Snapshot(index int, snapshot []byte) {
    // log.Printf("[Snapshot] Before lock, server=%d", rf.me)
    
    lockChan := make(chan bool, 1)
    go func() {
        rf.mu.Lock()
        lockChan <- true
    }()
    
    select {
    case <-lockChan:
        defer rf.mu.Unlock()
    case <-time.After(10*time.Second):
        log.Printf("[Snapshot] DEADLOCK DETECTED: Couldn't acquire lock after 10 seconds, server=%d", rf.me)
        return
    }
    
    // log.Printf("[Snapshot] After lock, server=%d, state=%d", rf.me, rf.serverState)
    
    if rf.logOffset >= index {
        // log.Printf("[Snapshot] Early return: logOffset=%d >= index=%d", rf.logOffset, index)
        return
    }
    
    // log.Printf("[Snapshot] Processing: index=%d, logOffset=%d, len(log)=%d", 
    //            index, rf.logOffset, len(rf.log))
    
    newIndex := index + 1 - rf.logOffset
	rf.lastIncludedTerm = rf.getLog(index).Term
    rf.logOffset = index
    rf.log = rf.log[newIndex:]
    rf.log = insert(rf.log, 0, Log{Term: rf.lastIncludedTerm, Command: nil})
	rf.snapshot = snapshot

    // log.Printf("[Snapshot] Complete: new logOffset=%d, len(log)=%d", 
    //            rf.logOffset, len(rf.log))
    // rf.PrintLog()
}


// example RequestVote RPC arguments structure.
// field names must start with capital letters!
type RequestVoteArgs struct {
	Term 			int
	CandidateId 	int
	LastLogIndex 	int
	LastLogTerm 	int
}

// example RequestVote RPC reply structure.
// field names must start with capital letters!
type RequestVoteReply struct {
	Term 			int
	VoteGranted 	bool
}

func (rf *Raft) checkIfWorthy(candidateId int, lastLogIndex int, lastLogTerm int) bool {
	if rf.votedFor != -1 && rf.votedFor != candidateId {
		return false
	}
	
	myLastLogIndex := rf.logOffset + len(rf.log) - 1
	myLastLogTerm := rf.getLog(myLastLogIndex).Term

	// If the logs have last entries with different terms, then the log with the later term is more up-to-date.
	if lastLogTerm < myLastLogTerm {
		return false
	}

	// If the logs end with the same term, then whichever log is longer is more up-to-date.
	if lastLogTerm == myLastLogTerm && lastLogIndex < myLastLogIndex {
		return false
	}

	return true
}

// example RequestVote RPC handler.
func (rf *Raft) RequestVote(args *RequestVoteArgs, reply *RequestVoteReply) {
	term := args.Term
	candidateId := args.CandidateId
	lastLogIndex := args.LastLogIndex
	lastLogTerm := args.LastLogTerm
	shouldPersist := false

	rf.mu.Lock()
	defer rf.mu.Unlock()
	// log.Printf("[RequestVote] candidate %d requesting for vote", args.CandidateId);

	reply.Term = rf.currentTerm
	// log.Printf("[RequestVote] in")
	if term < rf.currentTerm {
		reply.VoteGranted = false
		return
	}
	
	if term > rf.currentTerm {
		// log.Printf("[requestVote] term: %d, curr: %d", term, rf.currentTerm)
		rf.serverState = FOLLOWER
		rf.currentTerm = term
		rf.votedFor = -1
		rf.voteCount = 0
		reply.Term = rf.currentTerm
		shouldPersist = true
	}

	if rf.checkIfWorthy(candidateId, lastLogIndex, lastLogTerm) {
		reply.VoteGranted = true
		rf.votedFor = candidateId
		rf.lastBeat = time.Now()
		rf.persist()
		// log.Printf("[RequestVote] granting vote to candidate %d", args.CandidateId);
		return
	}

	// log.Printf("[RequestVote] not granting vote to candidate %d, my term %d, his term %d",
	//  args.CandidateId, rf.currentTerm, args.Term);

	reply.VoteGranted = false
	if shouldPersist{
		rf.persist()
	}
}

// example code to send a RequestVote RPC to a server.
// server is the index of the target server in rf.peers[].
// expects RPC arguments in args.
// fills in *reply with RPC reply, so caller should
// pass &reply.
// the types of the args and reply passed to Call() must be
// the same as the types of the arguments declared in the
// handler function (including whether they are pointers).
//
// The labrpc package simulates a lossy network, in which servers
// may be unreachable, and in which requests and replies may be lost.
// Call() sends a request and waits for a reply. If a reply arrives
// within a timeout interval, Call() returns true; otherwise
// Call() returns false. Thus Call() may not return for a while.
// A false return can be caused by a dead server, a live server that
// can't be reached, a lost request, or a lost reply.
//
// Call() is guaranteed to return (perhaps after a delay) *except* if the
// handler function on the server side does not return.  Thus there
// is no need to implement your own timeouts around Call().
//
// look at the comments in ../labrpc/labrpc.go for more details.
//
// if you're having trouble getting RPC to work, check that you've
// capitalized all field names in structs passed over RPC, and
// that the caller passes the address of the reply struct with &, not
// the struct itself.
func (rf *Raft) sendRequestVote(server int, args *RequestVoteArgs, reply *RequestVoteReply) bool {
	ok := rf.peers[server].Call("Raft.RequestVote", args, reply)
	return ok
}

/////////////////////////////////////////////////////////////////////////es arasworia/////////////////////////////////////////////////////////////////////
func (rf *Raft) sendLogs(server int, entries []Log){
	for{
		rf.mu.Lock()
		if rf.serverState != LEADER {
			rf.mu.Unlock()
			return
		}
		rf.mu.Unlock()

		prevLogIndex := rf.nextIndex[server] - 1

		args := AppendEntriesArgs{
			Term: rf.currentTerm,
			LeaderId: rf.me,
			PrevLogIndex: prevLogIndex,
			PrevLogTerm: rf.log[prevLogIndex].Term,
			Entries: entries,
			LeaderCommit: rf.commitIndex,
		}

		reply := AppendEntriesReply{}
		for {
			ok := rf.sendAppendEntries(server, &args, &reply)
			if ok {
				break
			}
		}
		term := reply.Term

		rf.mu.Lock()
		if term > rf.currentTerm {
			// log.Printf("[sendHeartBeat] term: %d, curr: %d", term, rf.currentTerm)
			rf.serverState = FOLLOWER
			rf.currentTerm = term
			rf.votedFor = -1
			rf.voteCount = 0
			rf.mu.Unlock()
			return
		}
		
		if reply.Success && reply.Term == rf.currentTerm {
			rf.updateMatchAndNextIndex(server, prevLogIndex, entries)
		} else if !reply.Success && len(entries) == 0 {
			rf.nextIndex[server] = max(1, rf.nextIndex[server] - 1)
			rf.mu.Unlock()
			continue
		}

		rf.commit(server)
		rf.mu.Unlock()
	}
}


func (rf *Raft) startLogging(command interface{}) {
	rf.mu.Lock()
	defer rf.mu.Unlock()

	entries := make([]Log, 1)
	entries[0] = Log{Term: rf.currentTerm, Command: command}

	for server, _ := range rf.peers {
		if server == rf.me {
			continue
		}

		go rf.sendLogs(server, entries)
	}
}
/////////////////////////////////////////////////////////////////////////es arasworia/////////////////////////////////////////////////////////////////////


// the service using Raft (e.g. a k/v server) wants to start
// agreement on the next command to be appended to Raft's log. if this
// server isn't the leader, returns false. otherwise start the
// agreement and return immediately. there is no guarantee that this
// command will ever be committed to the Raft log, since the leader
// may fail or lose an election. even if the Raft instance has been killed,
// this function should return gracefully.
//
// the first return value is the index that the command will appear at
// if it's ever committed. the second return value is the current
// term. the third return value is true if this server believes it is
// the leader.
func (rf *Raft) Start(command interface{}) (int, int, bool) {
	rf.mu.Lock()
	defer rf.mu.Unlock()

	index := rf.logOffset + len(rf.log)
	term := rf.currentTerm
	isLeader := (rf.serverState == LEADER)

	if isLeader {
		rf.matchIndex[rf.me] = index
		rf.log = append(rf.log, Log{Term: term, Command: command})
		rf.persist()
		// go rf.startLogging(command)
	}

	return index, term, isLeader
}

// the tester doesn't halt goroutines created by Raft after each test,
// but it does call the Kill() method. your code can use killed() to
// check whether Kill() has been called. the use of atomic avoids the
// need for a lock.
//
// the issue is that long-running goroutines use memory and may chew
// up CPU time, perhaps causing later tests to fail and generating
// confusing debug output. any goroutine with a long-running loop
// should call killed() to check whether it should stop.
func (rf *Raft) Kill() {
	atomic.StoreInt32(&rf.dead, 1)
	// Your code here, if desired.
}

func (rf *Raft) killed() bool {
	z := atomic.LoadInt32(&rf.dead)
	return z == 1
}

func (rf *Raft) sendRequest(server int, args *RequestVoteArgs, reply *RequestVoteReply){
	for {
		rf.mu.Lock()
		if args.Term != rf.currentTerm {
			rf.mu.Unlock()
			return
		}
		rf.mu.Unlock()
		ok := rf.sendRequestVote(server, args, reply)
		if ok {
			break
		}
		time.Sleep(10 * time.Millisecond)
	}

	voteGranted := reply.VoteGranted
	term := reply.Term

	rf.mu.Lock()
	defer rf.mu.Unlock()
	
	// log.Printf("[sendRequest] received"
	if term > rf.currentTerm {
		// log.Printf("[sendRequest] term: %d, curr: %d", term, rf.currentTerm)
		rf.serverState = FOLLOWER
		rf.currentTerm = term
		rf.votedFor = -1
		rf.voteCount = 0
		rf.persist()
		return
	}

	if rf.serverState != CANDIDATE || rf.currentTerm != args.Term || !voteGranted {
		return
	}

	rf.voteCount += 1
	if 2 * rf.voteCount > len(rf.peers) {
		// log.Printf("[sendRequest] server:%d term:%d", rf.me, rf.currentTerm)
		rf.serverState = LEADER
		for i := range rf.peers {
			rf.nextIndex[i] = rf.logOffset + len(rf.log)
			rf.matchIndex[i] = 0
			if rf.me == i {
				rf.matchIndex[i] = rf.logOffset + len(rf.log) - 1
			}
		}
	}
}

func (rf *Raft) sendRequests() {
	lastLogIndex := rf.logOffset + len(rf.log) - 1

	args := RequestVoteArgs{
		Term: rf.currentTerm, 
		CandidateId: rf.me,
		LastLogIndex: lastLogIndex,
		LastLogTerm: rf.getLog(lastLogIndex).Term,
	}

	for server, _ := range rf.peers {
		if server == rf.me {
			continue
		}

		reply := RequestVoteReply{}
		go rf.sendRequest(server, &args, &reply)
	}
	// log.Printf("[sendRequests] done")
}

func (rf *Raft) startElection() {
	rf.serverState = CANDIDATE
	rf.currentTerm += 1
	// log.Printf("[startElection] term: %d, server: %d", rf.currentTerm, rf.me)
	rf.votedFor = rf.me
	rf.voteCount = 1

	ms := MIN_ELEC_TIMEOUT + (rand.Int63() % ELEC_TIMEOUT_SIZE)
	rf.timeout = time.Duration(ms) * time.Millisecond
	rf.lastBeat = time.Now()

	rf.persist()
	rf.sendRequests()
}

func (rf *Raft) electionTicker() {
	for rf.killed() == false {
		time.Sleep(rf.timeout)

		rf.mu.Lock()
		// log.Printf("[electionTicker] starting new elections because last heartbeat %v and timeout %v", rf.lastBeat, timeout)
		if rf.serverState != LEADER && time.Since(rf.lastBeat) >= rf.timeout {
			rf.startElection()
		}
		rf.mu.Unlock()
	}
}
////////////////////////////////////////////////////SNAPSHOT///////////////////////////////////////////////////////
type SnapshotArgs struct {
	Term 				int
	LeaderId			int
	LastIncludedIndex 	int
	LastIncludedTerm	int
	Data				[]byte
}


type SnapshotReply struct {
	Term 				int
}

func (rf *Raft) InstallSnapshot(args *SnapshotArgs, reply *SnapshotReply) {
	term := args.Term
	// leaderId := args.LeaderId
	lastIncludedIndex := args.LastIncludedIndex
	lastIncludedTerm := args.LastIncludedTerm
	data := args.Data
	shouldPersist := false

	rf.mu.Lock()
	defer rf.mu.Unlock()

	reply.Term = rf.currentTerm

	if rf.currentTerm > term {
		return
	}

	if term > rf.currentTerm {
		rf.currentTerm = term
        rf.serverState = FOLLOWER
        rf.votedFor = -1
        rf.voteCount = 0
		shouldPersist = true
	}

	if rf.logOffset >= lastIncludedIndex {
		if shouldPersist {
			rf.persist()
		}
		return
	}

	diff := lastIncludedIndex - rf.logOffset
	newIndex := diff + 1

	newLog := make([]Log, 0)
	newLog = append(newLog, Log{Term: lastIncludedTerm, Command: nil})
	if newIndex <= len(rf.log) {
		newLog = append(newLog, rf.log[newIndex:]...)
	}
	rf.log = newLog
	rf.lastIncludedTerm = lastIncludedTerm
	rf.logOffset = lastIncludedIndex
	rf.snapshot = data
	rf.persist()
}

func (rf *Raft) sendInstallSnapshot(server int, args *SnapshotArgs, reply *SnapshotReply) bool {
	ok := rf.peers[server].Call("Raft.InstallSnapshot", args, reply)
	return ok
}

func (rf *Raft) sendSnapshot(server int) {
	args := SnapshotArgs{
		Term: rf.currentTerm,
		LeaderId: rf.me,
		LastIncludedIndex: rf.logOffset,
		LastIncludedTerm: rf.lastIncludedTerm,
		Data: rf.snapshot,
	}
	reply := SnapshotReply{}

	rf.mu.Unlock()
	ok := rf.sendInstallSnapshot(server, &args, &reply)
	if !ok {
		rf.mu.Lock()
		return
	}
	rf.mu.Lock()

	if reply.Term > rf.currentTerm {
		rf.currentTerm = reply.Term
        rf.serverState = FOLLOWER
        rf.votedFor = -1
        rf.voteCount = 0
		rf.persist()
	}

	if rf.currentTerm != args.Term || rf.serverState != LEADER {
		return
	}

	rf.matchIndex[server] = max(rf.matchIndex[server], rf.logOffset)
	rf.nextIndex[server] = max(rf.nextIndex[server], rf.logOffset + 1)
}

////////////////////////////////////////////////////SEND APPEND ENTRIES//////////////////////////////////////////////////
type AppendEntriesArgs struct {
	Term			int
	LeaderId		int
	PrevLogIndex	int

	PrevLogTerm		int
	Entries			[]Log
	LeaderCommit	int	
}

type AppendEntriesReply struct {
	Term 		int
	Success 	bool
	XTerm 		int
	XIndex		int
	XLen		int
}

func (rf *Raft) AppendEntries(args *AppendEntriesArgs, reply *AppendEntriesReply) {
    term := args.Term
    prevLogIndex := args.PrevLogIndex
    prevLogTerm := args.PrevLogTerm
    entries := args.Entries
    leaderCommit := args.LeaderCommit
    shouldPersist := false

    rf.mu.Lock()
    defer rf.mu.Unlock()

    reply.Term = rf.currentTerm
    reply.Success = false
    
    if term < rf.currentTerm {
        return
    }
    
    if term > rf.currentTerm {
        rf.currentTerm = term
        rf.serverState = FOLLOWER
        rf.votedFor = -1
        rf.voteCount = 0
        shouldPersist = true
    }

    rf.lastBeat = time.Now()

    if rf.logOffset + len(rf.log) <= prevLogIndex {
        reply.XLen = rf.logOffset + len(rf.log)
        reply.Success = false
        if shouldPersist {
            rf.persist()
        }
        return
    }

    if rf.getLog(prevLogIndex).Term != prevLogTerm {
        reply.XLen = -1
        reply.XTerm = rf.getLog(prevLogIndex).Term
        
        xindex := prevLogIndex - rf.logOffset
        for xindex > 0 && rf.log[xindex - 1].Term == reply.XTerm {
            xindex--
        }
        reply.XIndex = rf.logOffset + xindex
        if shouldPersist {
            rf.persist()
        }
        return
    }

    if len(entries) > 0 {
        rf.log = rf.log[:prevLogIndex + 1 - rf.logOffset]
        rf.log = append(rf.log, entries...)
        shouldPersist = true
    }

    if leaderCommit > rf.commitIndex {
        rf.commitIndex = min(leaderCommit, rf.logOffset + len(rf.log) - 1)
    }

    reply.Success = true
    if shouldPersist {
        rf.persist()
    }
}

func (rf *Raft) sendAppendEntries(server int, args *AppendEntriesArgs, reply *AppendEntriesReply) bool {
	ok := rf.peers[server].Call("Raft.AppendEntries", args, reply)
	return ok
}

func (rf *Raft) updateMatchAndNextIndex(server int, prevLogIndex int, entries []Log) {
	rf.matchIndex[server] = prevLogIndex + len(entries)
	rf.nextIndex[server] = rf.matchIndex[server] + 1
}

func (rf *Raft) calculateMedianMatchIndex() int {
	matchIndexCopy := append([]int(nil), rf.matchIndex...)
	matchIndexCopy[rf.me] = len(rf.log) - 1 + rf.logOffset
	sort.Ints(matchIndexCopy)
	return matchIndexCopy[(len(matchIndexCopy) - 1) / 2]
}

func (rf *Raft) commit(server int) {
	medianMatchIndex := rf.calculateMedianMatchIndex()
	if rf.commitIndex < medianMatchIndex && rf.log[medianMatchIndex - rf.logOffset].Term == rf.currentTerm {
		rf.commitIndex = medianMatchIndex
	}
}

func (rf *Raft) hasTerm(term int) bool {
    for _, entry := range rf.log {
        if entry.Term == term {
            return true
        }
    }
    return false
}

func (rf *Raft) getLastIndexForTerm(term int) int {
    for i := len(rf.log) - 1; i >= 0; i-- {
        if rf.log[i].Term == term {
            return i
        }
    }
    return -1
}

func (rf *Raft) updateNextIndex(server int, xlen int, xindex int, xterm int) {
    // Case 3: Follower's log is too short
    if xlen != -1 {
		rf.nextIndex[server] = xlen
		return
	}
	
	// Case 1: Leader doesn't have XTerm
    if !rf.hasTerm(xterm) {
        rf.nextIndex[server] = xindex
        return
    }
    
    // Case 2: Leader has XTerm
    lastIndexForTerm := rf.getLastIndexForTerm(xterm)
    if lastIndexForTerm != -1 {
        rf.nextIndex[server] = lastIndexForTerm + 1
        return
    }
}

func (rf *Raft) sendHeartBeat(server int) {
	rf.mu.Lock()
	currTerm := rf.currentTerm
	rf.mu.Unlock()

	for {
		rf.mu.Lock()
		if rf.serverState != LEADER || rf.currentTerm != currTerm {
			rf.mu.Unlock()
			return
		}
		prevLogIndex := rf.nextIndex[server] - 1
		prevLogTerm := rf.getLog(prevLogIndex).Term
		entries := make([]Log, 0)

		if rf.logOffset > prevLogIndex {
			rf.sendSnapshot(server)
			rf.mu.Unlock()
			return
		}

		//If last log index ≥ nextIndex for a follower: send AppendEntries RPC with log entries starting at nextIndex
		if rf.logOffset + len(rf.log) - 1 >= rf.nextIndex[server] {
			entries = append(entries, rf.log[rf.nextIndex[server] - rf.logOffset:]...)
		}

		args := AppendEntriesArgs{
			Term: rf.currentTerm,
			LeaderId: rf.me,
			PrevLogIndex: prevLogIndex,
			PrevLogTerm: prevLogTerm,
			Entries: entries,
			LeaderCommit: rf.commitIndex,	
		}

		reply := AppendEntriesReply{}

		rf.mu.Unlock()
		ok := rf.sendAppendEntries(server, &args, &reply)

		if !ok {
			time.Sleep(10 * time.Millisecond)
			continue
		}

		rf.mu.Lock()
		if reply.Term > rf.currentTerm {
			rf.serverState = FOLLOWER
			rf.votedFor = -1
			rf.currentTerm = reply.Term
			rf.voteCount = 0
			rf.persist()
		}
		
		// if len(entries) == 0 {
		// 	rf.mu.Unlock()
		// 	return 
		// }

		if rf.serverState != LEADER || rf.currentTerm != currTerm {
			rf.mu.Unlock()
			return
		}

		if reply.Success {
			// If successful: update nextIndex and matchIndex for follower
			rf.updateMatchAndNextIndex(server, args.PrevLogIndex, args.Entries)	
		} else {
			// If AppendEntries fails because of log inconsistency: decrement nextIndex and retry
			rf.updateNextIndex(server, reply.XLen, reply.XIndex, reply.XTerm)
			rf.mu.Unlock()
			time.Sleep(10 * time.Millisecond)
			continue
		}
		
		// log.Printf("[SENDING HEARTBEATS]")
		// rf.PrintLog()
		// rf.PrintMI()

		//If there exists an N such that N > commitIndex, a majority of matchIndex[i] ≥ N, and log[N].term == currentTerm: set commitIndex = N 
		rf.commit(server)
		rf.mu.Unlock()
		break
	}
}

func (rf *Raft) sendHeartBeats() {
	for server, _ := range rf.peers {
		if server == rf.me {
			continue
		}
		go rf.sendHeartBeat(server)
	}
	// log.Printf("[sendHeartBeats] done")
	// log.Printf("[heartBeatTicker] heartBeats send")
}

func (rf *Raft) heartBeatTicker() {
	for rf.killed() == false {
		rf.mu.Lock()
		if rf.serverState == LEADER {
			// log.Printf("[heartBeatTicker] heartBeats send time: %v", rf.lastBeat)
			rf.sendHeartBeats()
		}
		rf.mu.Unlock()

		time.Sleep(time.Duration(BEAT_TIME) * time.Millisecond)
	}
}

func (rf *Raft) applySnapshot() {
	msg := ApplyMsg{
		SnapshotValid: true,
		Snapshot: rf.snapshot,
		SnapshotIndex: rf.logOffset,
		SnapshotTerm: rf.lastIncludedTerm,
	}

	rf.lastApplied = rf.logOffset
	rf.mu.Unlock()
	rf.applyCh <- msg
	rf.mu.Lock()
}

func (rf *Raft) applyLogic() {
	if rf.lastApplied > rf.commitIndex {
		return
	}

	if rf.lastApplied < rf.logOffset {
		rf.applySnapshot()
		return
	}

	var messages []ApplyMsg
	for rf.commitIndex > rf.lastApplied {
		rf.lastApplied++
		msg := ApplyMsg{
			CommandValid: true,
			Command: rf.getLog(rf.lastApplied).Command,
			CommandIndex: rf.lastApplied,
		}
		messages = append(messages, msg)
	}
	rf.mu.Unlock()
	// log.Printf("[APPLYTICKER] lastApplied: %d", rf.lastApplied)
	// rf.PrintLog()
	for _, msg := range messages {
		// log.Printf("[MSG] cmd: %d, cmdI: %d", msg.Command, msg.CommandIndex)
		rf.applyCh <- msg
	}
	rf.mu.Lock()
}

func (rf *Raft) applyChTicker() {
    for !rf.killed() {
		
        rf.mu.Lock()
        rf.applyLogic()
		rf.mu.Unlock()

        time.Sleep(10 * time.Millisecond)
    }
}
 
// the service or tester wants to create a Raft server. the ports
// of all the Raft servers (including this one) are in peers[]. this
// server's port is peers[me]. all the servers' peers[] arrays
// have the same order. persister is a place for this server to
// save its persistent state, and also initially holds the most
// recent saved state, if any. applyCh is a channel on which the
// tester or service expects Raft to send ApplyMsg messages.
// Make() must return quickly, so it should start goroutines
// for any long-running work.
func Make(peers []*labrpc.ClientEnd, me int,
	persister *Persister, applyCh chan ApplyMsg) *Raft {
	rf := &Raft{}
	rf.peers = peers
	rf.persister = persister
	rf.me = me
	rf.applyCh = applyCh

	rf.currentTerm = 0
	rf.votedFor = -1
	
	rf.serverState = FOLLOWER
	rf.voteCount = 0
	rf.lastBeat = time.Now()

	rf.log = make([]Log, 0)
	rf.log = append(rf.log, Log{Term: 0, Command: nil})

	rf.nextIndex = make([]int, len(peers))
	rf.matchIndex = make([]int, len(peers))

	rf.commitIndex = 0
	rf.lastApplied = 0
	rf.logOffset = 0
	rf.lastIncludedTerm = 0

	ms := MIN_ELEC_TIMEOUT + (rand.Int63() % ELEC_TIMEOUT_SIZE)
	rf.timeout = time.Duration(ms) * time.Millisecond

	// initialize from state persisted before a crash
	rf.readPersist(persister.ReadRaftState())

	// start ticker goroutine to start elections
	go rf.electionTicker()
	go rf.heartBeatTicker()
	go rf.applyChTicker()

	return rf
}
