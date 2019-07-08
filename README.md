# kuda

Kuda is a simple workflow manager for micro services.

## Build

```bash
$ ./mvnw clean package 
$ docker build -f src/main/docker/Dockerfile.jvm -t gcr.io/{TAG_NAME} .
```

## Run

```bash
$ docker run -it -v ~/.secret/GCP/:/data -e GOOGLE_APPLICATION_CREDENTIALS=/data/{REDENTIAL}.json -p8080:8080 -e PORT=8080 -t gcr.io/{TAG_NAME}
```

## Workflow

Workflow is defined by following yaml file.
Please check `kuda.workflow` in `application.properties`.

```yaml
name: exec-batch
tasks:
  - name: step-load
    url: https://xxx-app1.run.app
  - name: step-parallel1
    dependencies: [step-load]
    url: https://xxx-app2.run.app
  - name: step-parallel2
    dependencies: [step-load]
    url: https://xxx-app3.run.app
  - name: step-join
    dependencies: [step-parallel1, step-parallel2]
    url: https://xxx-app4.run.app
```
