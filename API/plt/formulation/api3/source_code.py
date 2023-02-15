import logging
import pandas as pd
import csv
import ast
from collections import OrderedDict
import numpy as np
import copy
from operator import itemgetter

def RecExcipients(value, params): #value > 입력값, params > init_svc에서 가져온 params
    # 초기 변수 설정
    # params => "rxlist_df" : rxlist_new.csv, "change_log_df" : change_log_df
    rxlist_df = params['rxlist_df']
    change_log_df = params['change_log_df']
    chemical_df = params['chemical_df']
    routes_df = params['routes_df']

    '''
    1. input data 의 smiles 값을 알아낸다.
    2. rxlist 파일에서 ( Smiles1, Smiles2, Smiles3 ) 컬럼 중 하나의 컬럼이라도 smiles 와 값이 동일한 행의 INGREDIENT, KIND 값을 알아낸다. 리스트
    3. 2의 리스트 값 마다
       파일 `Change_Log_Data.xls` 의 컬럼 `Inactive Ingredient` 와 컬럼 `Dosage Form` 에 값이 들어 있는지 체크한다. 이후 해당 행의 컬럼(Maximum Potency per Unit Dose)을 리스트로 리턴한다.
       - 조건 : INGREDIENT in 컬럼 `Inactive Ingredient`, `입력값의 formulation` in 컬럼 `Dosage Form`
    4. 3에서 리턴한 값중 최소값, 최대값을 "use range" 에 매핑한다
      - e.g. "use range" = { 'max' : '0.03 %w/w', 'min' : '0.01 %w/w' }
    5. use range 의 max 를 숫자와 단위로 분리한다
      - "max":{'value':'0.3', 'unit':'%w/w'}
    6.
    '''
    # 예외처리, input값 error
    formulation_list = []
    key_smile= value['smiles']
    ## 1. input의 key(smiles, formulation, primary)가 누락된 경우
    if 'smiles' not in value or 'formulation' not in value or 'primary' not in value:
        code="001"
        msg="{smiles/formulation/primary}가 누락되었습니다."
        return (formulation_list, code, msg)
    ## 2. {smiles/formulation/primay}의 value가 누락된 경우
    elif len(value['smiles'])==0 or len(value['formulation'])==0 or len(value['primary'])==0 :
        code="002"
        msg="{smiles/formulation/primary}의 value가 누락되었습니다."
        return (formulation_list, code, msg)
    else:
        pass
    ## 4. formulation 값이 존재하지 않을때 (api2번에서 썼던 txt파일에 있는 투여경로에 따른 부형제 list에 존재하지 않는 경우)
    if (value['formulation']==routes_df['routes']).any():
        pass
    else:
        code="008"
        msg="입력하신 의약품의 적당한 부형제 목록이 존재하지 않습니다.\n 부형제 목록에 입력 요청 하시길 바랍니다. CODE:008"
        return (formulation_list, code, msg)
    ## 5. primary의 value 형식이 {'value' : 12, 'unit': 'ml'}가 아닌 경우
    if (('value' not in value['primary']) and ('unit' not in value['primary'])):
        code="009"
        msg="primary의 형식 중 value와 unit 값이 누락되었습니다. 다시 한번 확인해주세요."
        return (formulation_list, code, msg)
    elif ('unit' not in value['primary']):
        code="009"
        msg="primary의 형식 중 unit 값이 누락되었습니다. 다시 한번 확인해주세요."
        return (formulation_list, code, msg)
    elif ('value' not in value['primary']):
        code="009"
        msg="primary의 형식 중 value 값이 누락되었습니다. 다시 한번 확인해주세요."
        return (formulation_list, code, msg)
    else:
        pass
    # 이건 예외처리는 아니고 ai 모델이 개발되면 추후에 진행해야 할 부분, 입력받은 smiles string이 rxlist_df에 존재하지 않을 경우에는
    # 입력받은 smiles와 유사한 smiles를 rxlist_df의 smiles 열에서 찾아야함. 그럼 그에 맞는 부형제가 추천되어야함 하지만 아직, ai 모델 개발 진행 x
    is_smiles1 = rxlist_df['Smiles1'].str.contains(value['smiles'], na=False, regex=False)
    is_smiles2 = rxlist_df['Smiles2'].str.contains(value['smiles'], na=False, regex=False)
    is_smiles3 = rxlist_df['Smiles3'].str.contains(value['smiles'], na=False, regex=False)
    if rxlist_df[is_smiles1].empty and rxlist_df[is_smiles2].empty and rxlist_df[is_smiles3].empty:
        code="005"
        msg="입력하신 의약품의 적당한 부형제 목록이 존재하지 않습니다.\n 부형제 목록에 입력 요청 하시길 바랍니다. CODE:005"
    else:
        pass
    # 예외처리 끝

    ### change_log_df에서 ingredient를 ,별로 나누기 -> 새로운 파일 생성

    # step 1
    key_smile= value['smiles'] #smiles value 저장

    Smiles1 = rxlist_df['Smiles1'].str.contains(key_smile, na=False, regex=False)
    Smiles2 = rxlist_df['Smiles2'].str.contains(key_smile, na=False, regex=False)
    Smiles3 = rxlist_df['Smiles3'].str.contains(key_smile, na=False, regex=False)

    # step 2. Smiles1, Smiles2, Smiles3 값을 input smiles와 비교
    subset_df = rxlist_df[Smiles1 | Smiles2 | Smiles3]
    # step 2-1 unique, 중복제거
    distinct_df = subset_df[['INGREDIENT', 'KIND']].drop_duplicates()

    formulation = value["formulation"]

    # step 3
    # 결과값 저장
    formulation_list_clone = []
    formulation_list1 = []
    formulation_list2 = []
    formulation_list3 = []
    formulation_list4 = []
    formulation_dict = {}
    max_dict = {}
    result_list = []
    temp_list = []
    # 최대치용량, 숫자, 단위 분류

    # 사용범위, 최솟값, 최대값
    use_range_dict={}
    for i, row in distinct_df.iterrows() :
        formulation_dict.clear()
#         print('for문 밖에서 formulation_dict를 초기화함 : ', formulation_dict)
        formulation_length = len(formulation.split(','))
        for j in range(formulation_length) :
            temp_condition = None
#             print('j확인 : ', j)
            ingredient = distinct_df.at[i, "INGREDIENT"]
            kind = distinct_df.at[i, "KIND"]
            # kind가 nan인 경우 빈 값 처리, 플랫폼 내 오류 발생
            if type(kind) == float:
                kind = ''
            else:
                pass
            ## formulation 값이 하나만 들어온 경우
            if formulation.find(',') == -1:
                temp_condition = change_log_df.loc[(change_log_df['Inactive Ingredient'] == ingredient.upper()) & (change_log_df['Dosage Form'].str.contains(formulation.upper()))]
            else:
                # temp_condition = INGREDIENT in 컬럼 `Inactive Ingredient`, `입력값의 formulation` in 컬럼 `Dosage Form`
                # => ingredient와 입력받은 formulation이 모두 들어있는 경우
                temp_condition = change_log_df.loc[(change_log_df['Inactive Ingredient'] == ingredient.upper()) & (change_log_df['Dosage Form'].str.contains(str(formulation.split(',')[j].upper()).strip()))]
#                 print('temp_condition이 비어있는지 확인 : ', temp_condition.empty)
#                 print('입력받은 formulation : ', formulation.split(',')[j].upper())
            # temp_condition 조건에 만족한경우. 그에 맞는 max값을 찾아서 return
            if not temp_condition.empty:
#                 print('temp_condition이 채워진 경우 temp_condition : ', temp_condition[["Inactive Ingredient","Maximum Potency per Unit Dose"]])
                temp_df = temp_condition[["Inactive Ingredient","Maximum Potency per Unit Dose"]]
                # suspension을 타긴 함
                min = ''
                max = ''
                for _i, _row in temp_df.iterrows() :
                    mppud = temp_df.at[_i, "Maximum Potency per Unit Dose"]
                    if pd.isna(mppud):
                        pass
                        # pd에서 날려라
                    ## nan 체크
                    else:
                        mppud_value = float(mppud.split(' ')[0])
                        if max == '' or mppud_value > float(max.split(' ')[0]):
                            max = mppud
                        if min == '' or mppud_value < float(min.split(' ')[0]):
                            min = mppud
                if min == max and min != '':
                    min = '0'
                # result
                formulation_dict["excipients"] = ingredient
#                 print('temp_condition 조건에 맞는 경우 ingredient: ', ingredient)
                formulation_dict["kind"] = kind
                ## change_log_df에 ingredient와 formulation 모두 일치하지만 max 값이 존재하지 않을수도 있음
                if max == "":
                    max_value = ''
                    max_unit = ''
                else:
                    max_value = max
                    max_unit = 'mg'
                max_dict["value"]=max_value
                max_dict["unit"]=max_unit
                use_range_dict["min"]=min
                use_range_dict["max"]=max
                formulation_dict["max"]=max_dict
                formulation_dict["use range"]=use_range_dict
                # 결과값 dict를 list에 저장하여 한번에 출력하기 위해 dict를 copy하는 과정 필요
                formulation_list_clone.append(copy.deepcopy(formulation_dict))
                code="000"
                msg = "success"
                break
            # temp_condition 조건에 맞지 않는 경우, max와 use range를 빈값으로 return
            elif (temp_condition.empty and j == 1) or (temp_condition.empty and formulation_length == 1):
#                 print('temp_condition 조건에 맞지 않는 경우')
                formulation_dict["excipients"] = ingredient
#                 print('formulation_dict가 비워진 경우에 ingredient : ', ingredient)
                formulation_dict["kind"] = kind
                max_dict["value"]=""
                max_dict["unit"]=""
                use_range_dict["min"]=""
                use_range_dict["max"]=""
                formulation_dict["max"]=max_dict
                formulation_dict["use range"]=use_range_dict
                formulation_list_clone.append(copy.deepcopy(formulation_dict))
#                 print('formulation_dict가 비워진 경우에 formulation_list : ', formulation_list2)
                code = "000"
                msg = "success"
    return (formulation_list_clone, code, msg)