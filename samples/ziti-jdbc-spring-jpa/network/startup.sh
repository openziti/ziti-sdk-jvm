
if [ ! -f "simplified-docker-compose.yml" ]; then
   echo Pulling simplified-docker-compose.yml
   curl -o simplified-docker-compose.yml "https://raw.githubusercontent.com/openziti/ziti/release-next/quickstart/docker/simplified-docker-compose.yml"
fi


if [ ! -f ".env" ]; then
   echo "Pulling the docker environment file"
   curl -o .env https://raw.githubusercontent.com/openziti/ziti/release-next/quickstart/docker/.env
fi

echo Starting Ziti network
docker-compose -p zjpa -f simplified-docker-compose.yml -f docker-compose.yml up -d
