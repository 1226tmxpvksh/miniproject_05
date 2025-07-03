#!/bin/bash

# =================================================================
# MSA 프로젝트 전체 서비스 실행 스크립트
# =================================================================

# 1. (선택사항) 기반 인프라(Kafka 등)가 실행 중인지 확인
# init.sh를 먼저 실행했다면 이 부분은 이미 완료된 상태입니다.
# 혹시 모르니 docker-compose를 다시 실행해줍니다. (-d 옵션으로 백그라운드 실행)
echo "### Ensuring Docker infrastructure is running..."
(cd infra && docker-compose up -d)


# 2. 전체 프로젝트 빌드 (코드를 수정했을 경우를 대비해 항상 실행)
echo "### Building all projects with Maven... (This may take a moment)"
mvn clean install
if [ $? -ne 0 ]; then
    echo "----------------------------------------------------"
    echo "ERROR: Maven build failed. Please check the errors above."
    echo "----------------------------------------------------"
    exit 1
fi
echo "### Maven build successful."


# 3. 각 마이크로서비스를 백그라운드에서 실행
# 각 서비스는 application.yml에 지정된 포트로 실행됩니다.
echo "### Starting all microservices in the background..."

(cd gateway && mvn spring-boot:run) &
(cd book && mvn spring-boot:run) &
(cd user && mvn spring-boot:run) &
(cd subscription && mvn spring-boot:run) &
(cd writer && mvn spring-boot:run) &
(cd bestseller && mvn spring-boot:run) &
(cd openai && mvn spring-boot:run) &
(cd point && mvn spring-boot:run) &

echo ""
echo "############################################################"
echo "# All services are starting up."
echo "# It might take a minute for all services to be fully ready."
echo "#"
echo "# - To see logs for a specific service:"
echo "#   (e.g., for 'book' service)"
echo "#   docker logs -f <kafka_container_name> or check terminal tabs if using .gitpod.yml"
echo "#"
echo "# - To check running Java processes:"
echo "#   ps -ef | grep java"
echo "#"
echo "# - To stop all Java services:"
echo "#   killall java"
echo "############################################################"