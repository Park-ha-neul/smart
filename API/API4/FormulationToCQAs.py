import tensorflow as tf
import pandas as pd

def Formulation2CQAs(value, params):
    CQAs_List = []
    if 'formulation' in value:
        formulation_key = value['formulation'].title() #입력값이 소문자로만 들어와도 data 찾을 수 있게 title처리
        ## params = json file, params type = list params에서 각각의 dict를 비교
        for i in range(len(params)):
            if formulation_key in params[i]['formulation']:
                code = "000"
                msg = "success"
                CQAs_List.append(params[i]['CQAs'])
                break
            elif len(formulation_key)==0:
                code = "002"
                msg = "formulation의 value가 누락되었습니다."
            else:
               code="008"
               msg="존재하지 않는 부형제입니다. formulation을 다시 한번 확인해주세요."
    else:
        code = "001"
        msg = "formulation이 누락되었습니다."
    return (CQAs_List, code, msg)