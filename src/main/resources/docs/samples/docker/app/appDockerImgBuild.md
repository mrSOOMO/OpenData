## Running Your Application in Docker

The Docker image name should be the name of the Docker image that contains your application (in this case, `TwinklerApp`),
not the name of your application itself. This is typically the name you gave the image when you built it with
the `docker build` command, followed by a tag.

### Example:

```
docker run --name twinkler_app -p 8090:8090 -e POSTGRES_HOST=twinkler_db -e POSTGRES_PORT=5432 twinkler_app
```
The `twinkler_app:latest` part is the tag of the image, which defaults to latest
if not specified when the image is built.

If you have not built a Docker image for your application yet, you would typically do so with a command like:
```
docker build -t twinkler_app:latest .
```
This command tells Docker to build an image using the Dockerfile in the current directory (represented by the `.`),
give the image the name `twinklerapp`, and tag it as latest.
You would run this command from the root directory of your application, where the Dockerfile is located.

Then, you can run the image using the docker run command as shown above.
Please replace `twinkler_app:latest` with the actual name and tag of your Docker image.

### May the Force be with You!