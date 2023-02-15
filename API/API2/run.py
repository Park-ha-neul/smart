import logging
import pandas as pd
import csv
import ast
# local 에서 다른 파일을 import 해올 때는 아래와 같이 입력
from . import RoutesToFormulation as R2F
## 플랫폼에 등록할때 다른 파일을 import 해올 때는 아래와 같이 입력
# from RoutesToFormulation import *

# 학습 pass
def train(tm):
    pass

# 전처리 pass
def init_svc(im):
## 플랫폼에 등록할때 주석 해제
#     params = {"routes": [["routes_classification", "include"]], "oral": [["Capsule, Oral Capsule", "Capsule, Oral Capsule"], ["Tablet", "Tablet "], ["Oral Suspension, Suspension", "Oral Suspension, Suspension"], ["Oral Solution", "Oral solution, Elixir, Drop, Liquid, Syrup"], ["Granule", "Granule, Gum, Troche"], ["Powder", "Powder"], ["Gel", "Gel"], ["Sublingual", "Sublingual Tablet Subblingual Film, \nSubblingual Powder"], ["Sublingual Spray", "Subblingual Spray"], ["Bucal Tablet, Bucal Film", "Bucal Tablet & Bucal Film, Film"]], "local": [["Aerosol", "Injection"], ["Transmucosal Lozenge", "Intramuscular injection"], ["Topical Suspension, Topical Solution", "Intravenous Injection"], ["Cream, Topical Cream", "Implant, Subcutaneous Implant, \nSubcutaneous injection"], ["Emusion", "Injection Suspension"], ["Gel, Topical Gel", "Aerosol, Powder"], ["Lotion", "Transmucosal Lozenge"], ["Ointment", "Topical Suspension, Topical Solution"], ["Patch", "Cream, Topical Cream"], ["Shampoo, Topical Shampoo", "Emusion"], ["Nasal Spray, Nasal Solution", "Gel, Paste"], ["Spray", "Lotion"], ["Ophthalmic gel", "Ointment"], ["Ophthalmic solution", "Patch"], ["Ophthalmic suspension, Ophthalmic Emulsion", "Shampoo, Topical Shampoo"], ["INTRAVITREAL IMPLANT", "Nasal Spray, Nasal Solution"], ["Suppository", "Spray"], ["Suspension", "Ophthalmic gel"], ["Vaginal ", "Ophthalmic solution"], ["URETHRAL SUPPOSITORY", "Ophthalmic suspension, Ophthalmic Emulsion"]], "parenteral": [["Injection", "INTRAVITREAL IMPLANT"], ["Intramuscular injection", "Suppository"], ["Intravenous Injection", "Suspension"], ["Implant", "Vaginal Ring, Vaginal insert, \nVaginal gel, Vaginal Cream"], ["Injection Suspension", "URETHRAL SUPPOSITORY"]]}

#     return params
    pass

# 추론
def inference(df, params,batch_id):
    response = {}
    # df = 추론 테스트 시 입력 값 형태) [['입력 값']]
    value=df.values[0,0]
    # local 에서 다른 파일 import
    tuple = R2F.RoutesToFormulation(value, params)
    # 플랫폼 에서 다른 파일 import
#     tuple = RoutesToFormulation(value, params)
    res_dict=tuple[0]
    code = tuple[1]
    msg = tuple[2]
    try:
        response["result"]=res_dict
        response["code"]=code
        response["msg"]=msg
    except:
        code = "999"
        msg = "정의되지 않은 error입니다."
        response["code"]=code
        response["msg"]=msg

    return response