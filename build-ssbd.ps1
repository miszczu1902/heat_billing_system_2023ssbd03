Set-Location -Path ".\front\ssbd03front"
npm install
npm run build
Set-Location -Path "..\.."
docker-compose -f .\quarkus\docker-compose.yml up