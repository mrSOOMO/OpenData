If the file contains sensitive data like a private key, you should consider whether
 it's secure to include it in the Docker image, as anyone who has access to
  the Docker image could potentially extract the file. Instead, you could provide
  the file to the Docker container at runtime, for example, by using a Docker secret
   or a Kubernetes secret if you are deploying to Kubernetes. This way, the sensitive
   data isn't included in the Docker image itself and is only accessible
 to the running Docker container.



 src/main/resources/security/asymmetric_encryption/keys/privateKey.txt