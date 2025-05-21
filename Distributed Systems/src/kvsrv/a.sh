#!/bin/bash

for i in {1..100}
do
    echo "Run #$i"
    go test
    if [ $? -ne 0 ]; then
        echo "Test failed on run #$i"
        exit 1
    fi
done

echo "All 100 test runs passed successfully."