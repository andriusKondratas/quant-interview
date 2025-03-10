# quant-interview

## Intention

**Given** that a task is : [Requirements](Configuration/doc/task.txt)\
    **And** a time is very limited considering other commitments meanwhile\
Make some sacrifice on:
* **Obsession** on specific tech., which would require external plumbing and introduce a risk of not completing goal
* Pre-guess what **non-functional** requirements are
* **Documentation** over working software
* 100% Test coverage

Make no sacrifice on:
* Build, Test (at least success path), Works, Deployable 
* Does not require any external or major setup
* Doors are open for evolution without deleting everything 


## Build and Test

* Build test package and install to local mvn repository
```bash
john@pc:/quant-interview/Software/counter mvn clean package install -Pbuild-artifacts 
```

* Build docker images
```bash
john@pc:/quant-interview/Software/counter mvn jib:dockerBuild -Pbuild-image 
```

## Run
* Using IDE
  * Edit [Monitor variables](Software/counter/services/monitor-service/src/test/resources/application.properties)
  * Launch [Monitor service](Software/counter/services/monitor-service/src/main/java/org/example/monitor/MonitorApplication.java)
  * Feed some .csv files
  * Edit [Discovery variables](Software/counter/services/discovery-service/src/test/resources/application.properties)
  * Launch [Discovery service](Software/counter/services/discovery-service/src/main/java/org/example/discovery/DiscoveryApplication.java)

* Using images
```bash
john@pc:/quant-interview/Configuration/counter/local ./stack-up.sh
[+] Running 3/3
 ✔ Network quant-interview_default        Created                                                                                                                                                                     0.0s 
 ✔ Container quant-interview-monitor-1    Healthy                                                                                                                                                                    10.7s 
 ✔ Container quant-interview-discovery-1  Started
 
docker logs -f quant-interview-monitor-1
docker logs -f quant-interview-discovery-1

john@pc:/quant-interview/Configuration/counter/output tail -f top-connections.txt
 
john@pc:/quant-interview/Configuration/counter/local ./stack-down.sh
[+] Running 3/3
 ✔ Container quant-interview-discovery-1  Removed                                                                                                                                                                     0.2s 
 ✔ Container quant-interview-monitor-1    Removed                                                                                                                                                                     0.6s 
 ✔ Network quant-interview_default        Removed
 
docker volume rm quant-interview_counter-data       
```