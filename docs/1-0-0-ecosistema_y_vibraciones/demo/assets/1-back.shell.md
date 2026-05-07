```shell
## Backend
cd back

# Java Spring
curl https://start.spring.io/starter.zip \
  -d dependencies=web \
  -d type=maven-project \
  -d language=java \
  -d javaVersion=25 \
  -d groupId=academy.aicode \
  -d artifactId=astrobookings \
  -d name=astrobookings \
  -d packageName=academy.aicode.astrobookings \
  -o back.zip
unzip back.zip

# Node Express
curl -L https://github.com/AlbertoBasalo/express-boilerplate/archive/refs/heads/main.tar.gz \
  | tar -xz
  --strip-components=1
  -C ./back
```