# [과제] 스마트 제형설계 (AI기술 기반 스마트제형설계 및 제조공정 플랫폼 기술 개발)
- 목표
1. 약품 물질명으로부터 특성을 추론하여 적절한 투여경로와 구성 성분을 추천하는 제형설계
2. 결정된 성분들이 제조 가능한지 실험해 나가도록 제조공정 시스템 구축
3. 단순 UI를 제외한 모든 제약 업무 로직을 T3Q.dl의 추론API로 구현

- 기간
21.4.1 ~ 22.12.31 (21개월)
1차 마감일(10월 중순)

- 운영서버 정보(117.17.241.227)
ㅇ centos-release-7-9.2009.1.el7.centos.x86_64
ㅇ elrepo-release-7.0-5.el7.elrepo.noarch
ㅇ epel-release-7-11.noarch

# 실행환경 셋팅 - local에서 가상환경 새로 생성해서 다시 진행 후 upload 진행 예정
### pip install
```
$ pip install -r bak/requirements.txt
```
### local Anaconda 실행환경, env 생성 및 env list , activate
```
$ python version = 3.9.4
$ tensorflow version = 2.5.0
$ rdkit version = 2021.03.3
$ conda create -c conda-forge -n my-rdkit-env rdkit
$ conda activate my-rdkit-env
# local anaconda 실행환경
- my-rdkit-env 사용
```

## 패키지 설치
```
pip install pipenv (pipenv 설치가 되어있으면 건너뜀)
set PIPENV_VENV_IN_PROJECT=1 (현재 프로젝트 내부에 가상환경을 설치하는 환경변수)
pipenv --three (환경변수가 잡혀 있는 python3로 가상환경 공간 구성)
pipenv install (Pipfile에 적혀있는 파이썬 모듈을 생성)
```

### conda 환경에서 pip freeze > requirements.txt 시 '@file:///'을 생성하는 문제 해결
```
$ pip list --format=freeze > requirements.txt
```

### API1_type=chemical 테스트 시 주의 사항
- type별로 받는 path에 대한 공통경로는 로컬에서도 존재해야 함
- png를 만드는 과정 중에 sdf를 만들어야 하는 과정이 있는데, 이 부분에 대한 경로도 로컬에 존재해야함
- input으로 받는 path 폴더 : /data/test/
- png를 만드는 sdf path 폴더 : /data/aip/api/sdf/
- models 경로 : /data/aip/models/
### 변경사항
- 분자그림을 생성하기 위해 필요했던 sdf file은 분자그림을 생성하면 자동으로 삭제되는 코드를 추가함(용량만 차지)

## 제형 학습 알고리즘 설명
#####################
#1. FORMULATION_API1#
#####################
```
[FORMULATION_API1] - AI 모델
INPUT : type(chemical, smiles, sdf), value, path(분자그림 저장 경로)
OUTPUT : 물질의 구조, 특성 예측, 분자그림 생성 및 그에 따른 투여경로 추천

#설명
물질의 구조, 특성 분석 예측 모델을 통한 투여경로 추천
```

#####################
#2. FORMULATION_API2#
#####################
```
투여경로(routes) 에 따른 제형 추천 list를 출력하는 알고리즘.(formulation to routes)
1. 'routes' : oral
2. 'routes': local
3. 'routes': 'parenteral'
```

#####################
#3. FORMULATION_API3#
#####################
```
<<API3>>
1. smiles를 입력 값으로 받아서 (rxlist.csv)에서 일치하는 smiles 값의 ingredient와 kind를 return
    1) rxlist.csv에서 포함하지 않는 smiles인 경우, 인공지능 모델 MPN으로 벡터를 계산하여 rxlist의 smiles와 cosine 유사도를 계산한 후, 그와 가장 유사한 물질의 부형제를 return
2. return한 ingredient와 투여경로에 해당하는 제형(ex,Tablet)을 입력 값으로 받아서 (change_log.csv) 에서 입력 값(ingredient, 제형)과 일치하는 값의 min, max return

# 필요한 데이터 설명
root_dir = "/data/aip/csv/api3"
ㅇ rxlist new_20220616.csv : 입력 받은 smiles로 ingredient와 kind를 추출하기 위한 데이터
ㅇ change_log.csv : ingredient와 제형에 해당하는 최대, 최소를 추출하기 위한 데이터
ㅇ vectors_300cols : train()에서 mpn으로 계산한 각 물질의 300차원 vectors
ㅇ reg100/fold_0/model_0/model.pt : 모델 체크포인트
ㅇ routes.csv : 입력 받은 투여경로에 해당하는 제형의 존재 여부 확인 데이터

```

#####################
#4. FORMULATION_API4#
#####################
```
* 변경사항
api4 cqas list 한글 -> 영문 변환

제형선택에 따른 CQAs 실험방식 list 출력
ex) 성상, 제제균일성, 용출시험
1. formulation이 입력되지 않은 경우 (type error)
2. dataset에 없는 formulation이 입력된 경우 (value error)
>> 위 두가지 경우 error 출력
```

#####################
#5. FORMULATION_API5#
#####################
```
[FORMULATION_API5] - R 스크립트
INPUT : 부형제명과 부형제 사용량(최소, 최대)
OUTPUT : 실험설계법을 통한 실험치 입력 표

#설명
해당 입력값으로 R 스크립트를 돌려서 실험설계법(DoE - Design of Experiment) 진행
R 스크립트는 실험설계법 종류 중 요인설계법으로 되어있음
실험설계법 : 어떤 문제에 대해 실험을 통해 해결하고자 할 때, 실험을 계획하는 방법을 의미함
목적 : "최소 실험횟수"로 "최대의 정보"를 얻는 것

각각 부형제의 사용량에 대한 경우의 수를 구해준다고 생각하면 됨
ex) 부형제 3개를 입력받는데 각각 최소, 최대를 입력 받으니까 최대 경우의 수는 2^3 = 8가지임
그 후 최소, 최대의 중간치까지 합하여 최대 경우의 수를 구해주는 R 스크립트이다.
```
#####################
#6. FORMULATION_API6#
#####################
```
R 스크립트로 그래프 생성 API
[FORMULATION_API6] - R 스크립트
INPUT : API5{부형제명과 부형제 사용량(최소, 최대)}, 그래프 저장 경로(contour, response, pareto)
OUTPUT : 실험치 입력 값으로 그래프 생성(contour, response, pareto)

#설명
- API5번에서 실험설계법 데이터 생성 과정 동일하게 진행 필요
- INPUT으로 받은 실험치 입력 표로 그래프 생성 후 INPUT으로 받은 PATH 에 저장
```

#####################
#7. FORMULATION_API7#
#####################
```
R 스크립트로 그래프 생성 API
[FORMULATION_API6] - R 스크립트
INPUT : 실험방식 header값, API5{부형제명과 부형제 사용량(최소, 최대)}, 실험치 데이터, 그래프 저장 경로(design, result), 개별 응답값에 대한 목표차,
OUTPUT : 실험치 입력 값으로 그래프 생성(design, result{contour plot, desgin space}), 요인(변수)표, 부형제 결과값 표

#설명
- API5번에서 실험설계법 데이터 생성 과정 동일하게 진행 필요
- INPUT으로 받은 실험치 입력 데이터와 개별 응답값에 대한 목표치로 그래프 생성 후 INPUT으로 받은 PATH 에 저장
```

## 제조 학습 알고리즘 설명
#######################
#1. MANUFACTURING_API1#
#######################
```
* manufacturing_method
설명) properties 및 투여경로, 제형에 따른 제조방법 출력

input)
1. 제형설계 api1번에서 ai모델을 돌려서 나온 결과값(속성, properties)
2. 투여경로
3. 선택한 제형
4. 주성분용량

output)
input값에 따른 제조방법 (method)

주의사항)
input에서 제형 결과값인 properties를 모두 받는데,  {"Lipinski's Rule of 5": "Yes"} 이런식으로 작성된 부분을 입력받으면 형식에 맞지 않아서 error값이 나타남
이 값을 제외하고 입력받아야함
```

#######################
#2. MANUFACTURING_API2#
#######################
```
* manufacturing_list
설명) 투여경로, 제형, 제조방법에 따른 제조공정 목록 출력

input)
1. 투여경로
2. 제형
3. 선택한 제조방법

output)
input값에 따른 제조공정 목록 (list)
```

#######################
#3. MANUFACTURING_API3#
#######################
```
* manufacturing_list
설명)
투여경로, 제형, 제조방법, 제조공정, 실험방식에 따른 PHA 입력 테이블 출력

input)
1. 투여경로
2. 제형
3. 선택한 제조방법
4. 선택한 제조공정
5. 제형에서 선택한 실험방식(CQAs)

output)
1. 제조공정 가이드 데이터
2. PHA 입력 테이블에 해당하는 header값
```

#######################
#4. MANUFACTURING_API4#
#######################
```
* manufacturing_fmea
설명) 투여경로, 제형, 선택한 제조방법, 제조공정에 따른 FMEA 테이블, 유닛 공정 이미지 목록/상세
input)
1. 투여경로
2. 제형
3. 선택한 제조방법

output)
1. FMEA 테이블
2. 유닛 공정 이미지 목록/상세
```

###########################
#4-1. MANUFACTURING_API4-1#
###########################
```
* manufacturing_fmea
설명) 선택한 유닛 공정 이미지에 대한 공정 목록(CPP Factor) 출력

input)
1. 투여경로
2. 제형
3. 선택한 제조방법

output)
CPP FACTOR 목록 -> 사용가능 범위는 이번 과제에서 아예 제외
```

#######################
#5. MANUFACTURING_API5#
#######################
```
* manufacturing_CQAs
설명) 투여경로, 제형, 제조방법에 따른 CQAs 실험방식 출력

input)
1. 투여경로
2. 제형
3. 선택한 제조방법

output)
1. CQAs 실험방식
```

#######################
#6. MANUFACTURING_API6#
#######################
```
* manufacturing_experiment
설명) cpp명과 cpp 사용량에 따른 실험치 입력 표 출력

[MANUFACTURING_API6] - R 스크립트
INPUT : CPP명과 CPP 사용량(최소, 최대)
OUTPUT : 실험설계법을 통한 실험치 입력 표

#설명
해당 입력값으로 R 스크립트를 돌려서 실험설계법(DoE - Design of Experiment) 진행
R 스크립트는 실험설계법 종류 중 요인설계법으로 되어있음
실험설계법 : 어떤 문제에 대해 실험을 통해 해결하고자 할 때, 실험을 계획하는 방법을 의미함
목적 : "최소 실험횟수"로 "최대의 정보"를 얻는 것

각각 CPP의 사용량에 대한 경우의 수를 구해준다고 생각하면 됨
ex) CPP 3개를 입력받는데 각각 최소, 최대를 입력 받으니까 최대 경우의 수는 2^3 = 8가지임
그 후 최소, 최대의 중간치까지 합하여 최대 경우의 수를 구해주는 R 스크립트이다.

```

#######################
#7. MANUFACTURING_API7#
#######################
```
* manufacturing_contour
설명) 실험치 입력에 대한 그래프 출력
R 스크립트로 그래프 생성 API
[MANUFACTURING_API7] - R 스크립트
INPUT : API6{CPP명과 CPP 사용량(최소, 최대)}, 그래프 저장 경로(contour, response, pareto)
OUTPUT : 실험치 입력 값으로 그래프 생성(contour, response, pareto)

#설명
- API6번에서 실험설계법 데이터 생성 과정 동일하게 진행 필요
- INPUT으로 받은 실험치 입력 표로 그래프 생성 후 INPUT으로 받은 PATH 에 저장
```

#######################
#8. MANUFACTURING_API8#
#######################
```
* manufacturing_result
설명) 실험치 입력에 대한 그래프 및 결과값 출력

R 스크립트로 그래프 생성 API
[MANUFACTURING_API8] - R 스크립트

INPUT)
1. 실험방식 header값
2. API6 CPP명과 CPP 사용량(최소, 최대)
3. 실험치 데이터
4. 그래프 저장 경로 (design, result)
5. 개별 응답값에 대한 목표치

OUTPUT)
1. 그래프 생성 (contour plot, design space)
2. 요인변수표
3. cpp 결과값 표

#설명
- API6번에서 실험설계법 데이터 생성 과정 동일하게 진행 필요
- INPUT으로 받은 실험치 입력 데이터와 개별 응답값에 대한 목표치로 그래프 생성 후 INPUT으로 받은 PATH 에 저장
```
