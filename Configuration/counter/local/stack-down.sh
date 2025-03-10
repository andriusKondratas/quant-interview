#!/bin/bash
source stack.env

docker compose -f stack.yml --env-file stack.env down --remove-orphans
