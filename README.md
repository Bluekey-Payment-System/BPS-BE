[![GitHub Stars](https://img.shields.io/github/stars/Bluekey-Payment-System/BPS-BE?style=for-the-badge)](https://github.com/Bluekey-Payment-System/BPS-BE/stargazers) 
[![GitHub Issues](https://img.shields.io/github/issues/Bluekey-Payment-System/BPS-BE?style=for-the-badge)](https://github.com/Bluekey-Payment-System/BPS-BE/issues) 
[![Current Version](https://img.shields.io/badge/version-1.0.0-black?style=for-the-badge)](https://github.com/Bluekey-Payment-System/BPS-BE) 
[![GitHub License](https://img.shields.io/github/license/Bluekey-Payment-System/BPS-BE?style=for-the-badge)](https://github.com/Bluekey-Payment-System/BPS-BE/blob/main/LICENSE)

<br />
<br />

<div align="center">
<img src= "docs/images/logo.png" alt = "logo" style="width: 200px" />
<br />

`블루키뮤직` 음악 제작사와 계약 아티스트 간의 정산 세부 내역 및 현황을 투명하게 공개하고, 
<br />
매출 추이 등의 통계 지표를 볼 수 있는 정산 플랫폼 사이트입니다.

<br />

<div align="left">

> <h3> Development Emphasis </h3> <hr />
> In the development of the service, I have placed a strong emphasis on 'security' and the 'separation' of the testing environment. It is crucial to ensure that the data delivered to artists is 'accurately calculated', given the importance of this aspect.

</div>
</div>

<br />

# Architecture
## Infra
<img src="https://img.shields.io/badge/Amazon AWS-232F3E?style=flat-square&logo=amazonaws&logoColor=white" alt="amazon"/>
<img src="https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=Docker&logoColor=white" alt="docker"/>
<img src="https://img.shields.io/badge/GitHub Actions-181717?style=flat-square&logo=GitHub&logoColor=white" alt="github actions"/>

<img src="docs/images/infra.png" alt = "Infra" />

### AWS
- EC2
  - T2.micro
  - Amazon Linux 2023
  - `Docker container` 3개 가동
    - Production Server
    - Dev Server
    - Dev Server MySQL
- Route53
  - ALB 등록 및 name server를 Front 프리티어 계정의 Route53에 등록
- S3
  - Profile Image bucket
  - Excel File bucket
- RDS
  - Production server MySQL

### Docker & Github actions
- `target branch`가 `main branch`인 경우 Production Server image build 및 EC2에서 docker container (production server) 가동
- `target branch`가 `develop branch`인 경우 Develop Server image build 및 EC2에서 docker container (dev server, DB) 가동
- `Github actions`를 통해서 자동화 배포

<br />

## Server