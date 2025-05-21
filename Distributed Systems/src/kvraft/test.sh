#!/bin/bash

# Check if a test name was provided
if [ -z "$1" ]; then
  echo "Usage: $0 <test name>"
  echo "Example: $0 3A"
  exit 1
fi

TEST_NAME="$1"  # Get the test name from the first argument
LOGFILE="logfile.log"  # Set a unique log file name based on the test name

# Clear previous log file (optional)
> "$LOGFILE"

# Number of iterations for each test
if [ "$TEST_NAME" == "3A" ]; then
  ITERATIONS=100
elif [ "$TEST_NAME" == "3B" ] || [ "$TEST_NAME" == "3C" ] || [ "$TEST_NAME" == "3D" ]; then
  ITERATIONS=100
elif [ "$TEST_NAME" == "4A" ] || [ "$TEST_NAME" == "4B" ]; then
  ITERATIONS=100
else
  echo "Invalid test name. Use 3A, 3B, 3C or 3D."
  exit 1
fi

# Loop for the specified test
for i in $(seq 1 $ITERATIONS); do
  echo "Running test iteration $i for $TEST_NAME" | tee -a "$LOGFILE"
  { time go test -run "$TEST_NAME" ; } 2>&1 | tee -a "$LOGFILE"
  
  # Stop if a test fails
  if grep -q "FAIL" "$LOGFILE"; then
    echo "Test failed in iteration $i for $TEST_NAME. Stopping the process."
    # exit 1
  fi

  echo "----------------------------------------" | tee -a "$LOGFILE"
done

echo "All $TEST_NAME test runs completed successfully." | tee -a "$LOGFILE"
