# Data Ingestion Platform

Please use this readme as your projects readme. You can find instructions for
the challenge in the [`INSTRUCTIONS.pdf`](INSTRUCTIONS.pdf) file.

## LA MetroAPI

Unfortunatelly, LA MetroAPI is no longer available. We, nonetheless, provide a docker container
that generates random data to be used along this exercise.

To pull and run this docker image, do:

```sh
$ docker pull ghcr.io/lunatech-labs/lunatech-data-ingestion-platform-metroapi:1.0
$ docker run -p "3000:3000" ghcr.io/lunatech-labs/lunatech-data-ingestion-platform-metroapi:1.0
```

After the container is running properly, you can access its data at [http://localhost:3000/api](http://localhost:3000/api)
