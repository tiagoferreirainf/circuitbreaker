#!/bin/bash

set -B                  # enable brace expansion
for i in {1..30}; do
  sleep 1
  curl 'http://localhost:8082/serviceb/echo/' &
done