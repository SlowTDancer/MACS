package mr

import "log"
import "net"
import "os"
import "net/rpc"
import "net/http"
import "time"
import "sync"

const TimeToWait = 10


type Task struct{
	Filename string
	Status string
	StartTime time.Time
	Index int
	NReduce int
}

type Coordinator struct {
	mapTasks []Task
	reduceTasks []Task
	lock sync.Mutex
}

func (c *Coordinator) DistributeTasks(args *Args, reply *Reply) error {
	c.lock.Lock()
	defer c.lock.Unlock()

	taskType, task := c.CheckMapTasks()
	if taskType != "NONE" {
		reply.Task = task
		reply.TaskType = taskType
		return nil
	}

	// log.Printf("[DISTRIBUTETASKS] morcha map!!!")
	taskType, task = c.CheckReduceTasks()
	if taskType != "NONE" {
		reply.Task = task
		reply.TaskType = taskType
		return nil
	}

	reply.Task = Task{} 
	reply.TaskType = "NONE"
	return nil
}



func (c *Coordinator) CheckMapTasks() (string, Task) {
	taskType := "NONE"
	for i := range c.mapTasks {
		task := &c.mapTasks[i]
		if task.Status == "NOT STARTED" {
			task.Status = "RUNNING"
			task.StartTime = time.Now()
			return "MAP", *task
		}

		if task.Status == "RUNNING" && time.Since(task.StartTime) > time.Second * TimeToWait {
			task.StartTime = time.Now()
			return "MAP", *task
		}else if task.Status == "RUNNING"{
			taskType = "WAIT"
		}
	}
	
	return taskType, Task{} 
}



func (c *Coordinator) CheckReduceTasks() (string, Task){
	taskType := "NONE"
	for i := range c.reduceTasks {
		task := &c.reduceTasks[i]
		if task.Status == "NOT STARTED" {
			task.Status = "RUNNING"
			task.StartTime = time.Now()
			return "REDUCE", *task
		}

		if task.Status == "RUNNING" && time.Since(task.StartTime) > time.Second * TimeToWait {
			task.StartTime = time.Now()
			return "REDUCE", *task
		}else if task.Status == "RUNNING"{
			taskType = "WAIT"
		}
	}
	
	return taskType, Task{} 
}


func (c *Coordinator) TaskDone(args *Args, reply *Reply) error{
	c.lock.Lock()
	defer c.lock.Unlock()

	if args.TaskType == "MAP" {
		task := &c.mapTasks[args.Task.Index]
		task.Status = "COMPLETED"
		return nil
	}

	task := &c.reduceTasks[args.Task.Index]
	task.Status = "COMPLETED"
	return nil
}


//
// an example RPC handler.
//
// the RPC argument and reply types are defined in rpc.go.
//
func (c *Coordinator) Example(args *ExampleArgs, reply *ExampleReply) error {
	reply.Y = args.X + 1
	return nil
}



//
// start a thread that listens for RPCs from worker.go
//
func (c *Coordinator) server() {
	rpc.Register(c)
	rpc.HandleHTTP()
	//l, e := net.Listen("tcp", ":1234")
	sockname := coordinatorSock()
	os.Remove(sockname)
	l, e := net.Listen("unix", sockname)
	if e != nil {
		log.Fatal("listen error:", e)
	}
	go http.Serve(l, nil)
}

//
// main/mrcoordinator.go calls Done() periodically to find out
// if the entire job has finished.
// reduce taskebi tu morchenilia maptaskebic morchenili iqneba da magas vamowmeb
//
func (c *Coordinator) Done() bool {
	c.lock.Lock()
	defer c.lock.Unlock()

	for _, task := range c.reduceTasks {
		if task.Status != "COMPLETED" {
			return false 
		}
	}

	return true
}


//
// create a Coordinator.
// main/mrcoordinator.go calls this function.
// nReduce is the number of reduce tasks to use.
//
func MakeCoordinator(files []string, nReduce int) *Coordinator {
	c := Coordinator{}

	c.mapTasks = make([]Task, len(files))
	for i, filename := range files {
		c.mapTasks[i] = Task{
			Filename:  filename,
			Status:    "NOT STARTED",
			StartTime: time.Now(),
			Index:     i,
			NReduce:   nReduce,
		}
	}

	c.reduceTasks = make([]Task, nReduce)
	for i := 0; i < nReduce; i++ {
		c.reduceTasks[i] = Task{
			Filename:  "",
			Status:    "NOT STARTED",
			StartTime: time.Now(),
			Index:     i,
			NReduce:   nReduce,
		}
	}

	c.server()
	return &c
}

