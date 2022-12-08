#!/bin/bash

set -B                  # enable brace expansion
for i in {1..5}; do
  echo "Going for request number "$i
  curl 'http://localhost:8082/serviceb/retry/'$i
  echo ""
done