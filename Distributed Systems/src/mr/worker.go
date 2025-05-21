package mr

import (
	"fmt"
	"log"
	"os"
	"io/ioutil"
	"net/rpc"
	"hash/fnv"
	"sort"
	"encoding/json"
	"time"
	"regexp"
)

// 
// some constants
// 
const TimeToSleep = 1

//
// Map functions return a slice of KeyValue.
//
type KeyValue struct {
	Key   string
	Value string
}

// for sorting by key.
type ByKey []KeyValue

// for sorting by key.
func (a ByKey) Len() int           { return len(a) }
func (a ByKey) Swap(i, j int)      { a[i], a[j] = a[j], a[i] }
func (a ByKey) Less(i, j int) bool { return a[i].Key < a[j].Key }

//
// use ihash(key) % NReduce to choose the reduce
// task number for each KeyValue emitted by Map.
//
func ihash(key string) int {
	h := fnv.New32a()
	h.Write([]byte(key))
	return int(h.Sum32() & 0x7fffffff)
}

//
// main/mrworker.go calls this function.
// aq maqvs aseti raghaca
// input --(map)--> intermediate --(reduce)--> output
//
func Worker(mapf func(string, string) []KeyValue,
	reducef func(string, []string) string) {

	for {
		args := Args{}
		reply := Reply{}
		ok := call("Coordinator.DistributeTasks", &args, &reply)
		if !ok {
			fmt.Printf("call failed!\n")
			continue
		}
		// log.Printf("[WORKER] reply - type: %v, filename: %v", reply.TaskType, reply.Task.Filename)
		
		if reply.TaskType == "MAP" {
			handleMap(mapf, reply.Task)
		} else if reply.TaskType == "REDUCE" {
			handleReduce(reducef, reply.Task)
		} else if reply.TaskType == "WAIT" {
			time.Sleep(time.Second * TimeToSleep)
			continue
		}else{
			break
		}

		args = Args{TaskType: reply.TaskType, Task: reply.Task}
		reply = Reply{}
		ok = call("Coordinator.TaskDone", &args, &reply)
		if !ok {
			fmt.Printf("call failed!\n")
			continue
		}
	}

	// uncomment to send the Example RPC to the coordinator.
	// CallExample()
}

func handleMap(mapf func(string, string) []KeyValue, task Task) {
	content := readInputFile(task.Filename)
	kva := mapf(task.Filename, content)
	writeIntermediateFile(task, kva)
	// log.Printf("[HANDLEMAP] mapi morcha!!!")
}

func readInputFile(filename string) string {
	file, err := os.Open(filename)
	if err != nil {
		log.Fatalf("cannot open %v", filename)
	}

	content, err := ioutil.ReadAll(file)
	if err != nil {
		log.Fatalf("cannot read %v", filename)
	}

	file.Close()
	return string(content)
}

func writeIntermediateFile(task Task, kva []KeyValue) {
    tempFiles := make(map[int]*os.File)
    encoders := make(map[int]*json.Encoder)

    for _, kv := range kva {
        nBucket := ihash(kv.Key) % task.NReduce

        if _, exists := encoders[nBucket]; !exists {
            tempFile, err := os.CreateTemp("", fmt.Sprintf("mr-%d-%d-*", task.Index, nBucket))
            if err != nil {
                log.Fatalf("[WIF] Cannot create temp file: %v", err)
            }

            tempFiles[nBucket] = tempFile
            encoders[nBucket] = json.NewEncoder(tempFile)
        }

        err := encoders[nBucket].Encode(&kv)
        if err != nil {
            log.Fatalf("[WIF] Failed to write to temp file: %v", err)
        }
    }

    for nBucket, tempFile := range tempFiles {
        finalName := fmt.Sprintf("mr-%d-%d", task.Index, nBucket)

        err := os.Rename(tempFile.Name(), finalName)
        if err != nil {
            log.Fatalf("[WIF] Failed to rename temp file to final name: %v", err)
        }

		tempFile.Close()
    }
}


func handleReduce(reducef func(string, []string) string, task Task) {
	content := readIntermediateFiles(task.Index)
	sort.Sort(ByKey(content))
	writeOutputFile(reducef, content, task)
	// log.Printf("[HANDLEREDUCE] reduce morcha!!!")
}


func readIntermediateFiles(taskIndex int) []KeyValue {
	files, err := ioutil.ReadDir(".")
	if err != nil {
		log.Fatalf("cannot read directory: %v", err)
	}

	var content []KeyValue

	for _, file := range files {
		if matched, _ := regexp.MatchString(fmt.Sprintf(`^mr-\d+-%d$`, taskIndex), file.Name()); matched {
			filePath := file.Name()
			fileContent := readIntermediateFile(filePath)
			content = append(content, fileContent...)
		}
	}

	return content
}

func readIntermediateFile(filename string) []KeyValue {
	file, err := os.Open(filename)
	if err != nil {
		log.Fatalf("cannot open %v", filename)
	}
	defer file.Close()

	var content []KeyValue
	dec := json.NewDecoder(file)
	for {
		var kv KeyValue
		if err := dec.Decode(&kv); err != nil {
			break
		}
		content = append(content, kv)
	}

	return content
}


func writeOutputFile(reducef func(string, []string) string, content []KeyValue, task Task) {
	outputFilename := fmt.Sprintf("mr-out-%d", task.Index)
	tempfile, err := os.CreateTemp("",  fmt.Sprintf("mr-out-%d-*", task.Index))
	if err != nil {
		log.Fatalf("[WOF] Failed to create temp file: %v", err)
	}
	defer tempfile.Close()

	i := 0	
	for i < len(content) {
		j := i + 1
		for j < len(content) && content[j].Key == content[i].Key {
			j++
		}
		values := []string{}
		for k := i; k < j; k++ {
			values = append(values, content[k].Value)
		}
		output := reducef(content[i].Key, values)

		// this is the correct format for each line of Reduce output.
		fmt.Fprintf(tempfile, "%v %v\n", content[i].Key, output)

		i = j
	}

	err = os.Rename(tempfile.Name(), outputFilename)
	if err != nil {
		log.Fatalf("[WOF] Failed to rename file: %v", err)
	}
}


//
// example function to show how to make an RPC call to the coordinator.
//
// the RPC argument and reply types are defined in rpc.go.
//
func CallExample() {

	// declare an argument structure.
	args := ExampleArgs{}

	// fill in the argument(s).
	args.X = 99

	// declare a reply structure.
	reply := ExampleReply{}

	// send the RPC request, wait for the reply.
	// the "Coordinator.Example" tells the
	// receiving server that we'd like to call
	// the Example() method of struct Coordinator.
	ok := call("Coordinator.Example", &args, &reply)
	if ok {
		// reply.Y should be 100.
		fmt.Printf("reply.Y %v\n", reply.Y)
	} else {
		fmt.Printf("call failed!\n")
	}
}


//
// send an RPC request to the coordinator, wait for the response.
// usually returns true.
// returns false if something goes wrong.
//
func call(rpcname string, args interface{}, reply interface{}) bool {
	// c, err := rpc.DialHTTP("tcp", "127.0.0.1"+":1234")
	sockname := coordinatorSock()
	c, err := rpc.DialHTTP("unix", sockname)
	if err != nil {
		log.Fatal("dialing:", err)
	}
	defer c.Close()

	err = c.Call(rpcname, args, reply)
	if err == nil {
		return true
	}

	fmt.Println(err)
	return false
}
