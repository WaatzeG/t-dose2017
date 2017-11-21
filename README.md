# t-dose2017

This repo contains the [slides](/Apache%20Storm.odp) for the talk I gave.

## Demo code
In order to use the demo, spin a Docker environment up consisting of a Apache Storm cluster. You can use the docker-compose.yml file in the `docker` directory.

Use the command: `docker-compose up --force-recreate` while in that directory. This will create:
* Storm Nimbus
* 2x Storm Supevisors
* Storm UI Reachable through http://localhost:8080
* Redis
* Webviewer to view top 10 words in Shakespeare from the demo. Reachable through http://localhost:8001
* Zookeeper cluster (requirement for Apache Storm)

The Apache Storm demo code can be found in the directory [src](/src)

## Run the demo
Compile the demo using:
`gradle build` in the rootfolder

When a local Storm cluster is running in Docker (see previous section) upload the compiled Storm topology using:

`deploy/deploy_topology.sh`

When done, you can either kill the topology in the Storm UI or through `docker/kill_topology.sh`

## Demo results
Open http://localhost:8001 to view the top 10 words in Shakespeare from Redis.

Open http://localhost:8080 to view the Storm UI
