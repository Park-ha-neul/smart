import sqlite3
import functools
import operator
import os
import shutil
conn = sqlite3.connect('/data/aip/activate/manufacturing/api4/fmea.db', check_same_thread=False)

# 커서 바인딩
c = conn.cursor()

# db 조회
def db(value) :
    factor_list = []
    result_factor = {}
    route = value['routes']
    formulation = value['formulation']
    method = value['method']
    level = value['level']
    if route == 'oral':
        oral_formulation_list = ['Capsule, Oral Capsule', 'Tablet', 'Oral Suspension, Suspension', 'Oral Solution', 'Granule', 'Powder', 'Gel', 'Sublingual', 'Sublingual Spray', 'Bucal Tablet, Bucal Film']
        oral_method_list = ['직타', '습식과립', '건식과립', '액상제조법', '에멀전제조법', '건조시럽']
        if formulation not in oral_formulation_list:
            code = "005"
            msg = "투여경로에 맞는 제형을 입력해주세요."
        elif method not in oral_method_list:
            code = "007"
            msg = "제조방법을 다시 확인해주세요."
        else:
            ################
            # STEP1. DB 조회#
            ################
            for i in range(len(level)):
                ## 1. fmea 테이블 DB 조회(입력한 제형, 제조방법, 제조공정에 따른 )
                oral_select = "SELECT DISTINCT level, cpps FROM fmea_oral WHERE FORMULATION LIKE '%" + formulation + "%' AND METHOD LIKE '" + method + "%' AND LEVEL LIKE '" + level[i] +"%' ORDER BY id"
                c.execute(oral_select)
                result = c.fetchall()
                if result == []:
                    code = "009"
                    msg = "제조공정을 다시 확인해주세요."
                    return factor_list, code, msg
                else:
                    for i in result:
                        result_factor.setdefault(i[0],[]).append(i[1])
            factor_list.append(result_factor)
            code = "000"
            msg = "success"
    elif route == 'parenteral':
        parenteral_formulation_list = ['Injection', 'Intramuscular injection', 'Intravenous Injection', 'Implant', 'Injection Suspension']
        parenteral_method_list = ['액상제조법', '에멀전제조법', '분말제조법', '동결건조제조법']
        if formulation not in parenteral_formulation_list:
            code = "005"
            msg = "투여경로에 맞는 제형을 입력해주세요."
        elif method not in parenteral_method_list:
            code = "007"
            msg = "제조방법을 다시 확인해주세요."
        else:
            ################
            # STEP1. DB 조회#
            ################
            for i in range(len(level)):
                ## 1. fmea 테이블 DB 조회(입력한 제형, 제조방법, 제조공정에 따른 level, cpps 출력)
                parenteral_select = "SELECT DISTINCT level, cpps FROM fmea_parenteral WHERE FORMULATION LIKE '%" + formulation + "%' AND METHOD LIKE '" + method + "%' AND LEVEL LIKE '" + level[i] +"%' ORDER BY id"
                c.execute(parenteral_select)
                result = c.fetchall()
                if result == []:
                    code = "009"
                    msg = "제조공정을 다시 확인해주세요."
                    return factor_list, code, msg
                else:
                    for i in result:
                        result_factor.setdefault(i[0],[]).append(i[1])
            factor_list.append(result_factor)
            code = "000"
            msg = "success"

    elif route == 'local':
        local_formulation_list = ['Aerosol', 'Transmucosal Lozenge', 'Topical Suspension, Topical Solution', 'Cream, Topical Cream', 'Emusion', 'Gel, Topical Gel', 'Lotion', 'Ointment', 'Patch', 'Shampoo, Topical Shampoo', 'Nasal Spray, Nasal Solution', 'Spray', 'Ophthalmic Gel', 'Ophthalmic Solution', 'Ophthalmic Suspension, Ophthalmic Emulsion', 'Intravitreal Implant', 'Suppository', 'Suspension', 'Vaginal', 'Urethral Suppository']
        local_method_list = ['액상제조법', '에멀전제조법', '분말제조법', '동결건조제조법', '현탁액제조법', '연고제조법']
        if formulation not in local_formulation_list:
            code = "005"
            msg = "투여경로에 맞는 제형을 입력해주세요."
        elif method not in local_method_list:
            code = "007"
            msg = "제조방법을 다시 확인해주세요."
        else:
            ################
            # STEP1. DB 조회#
            ################
            for i in range(len(level)):
                ## 1. fmea 테이블 DB 조회(입력한 제형, 제조방법, 제조공정에 따른 cpp, failure, effect 출력)
                local_select = "SELECT DISTINCT level, cpps FROM fmea_local WHERE FORMULATION LIKE '%" + formulation + "%' AND METHOD LIKE '" + method + "%' AND LEVEL LIKE '" + level[i] +"%' ORDER BY id"
                c.execute(local_select)
                result = c.fetchall()
                if result == []:
                    code = "009"
                    msg = "제조공정을 다시 확인해주세요."
                    return factor_list, code, msg
                else:
                    for i in result:
                        result_factor.setdefault(i[0],[]).append(i[1])
            factor_list.append(result_factor)
            code = "000"
            msg = "success"
        # 투여경로를 잘못 입력한 경우, 투여경로는 총 3가지 중 하나를 입력할 수 있음(oral, parenteral, local)
    else:
        code = "001"
        msg = "투여경로를 잘못 입력하였습니다."

    return factor_list, code, msg

# 인터페이스 정의에 따른 틀 생성
def frame(value, params):
    factor_list, code, msg = db(value)
    res_dict = {}
    res_dict["factor"] = factor_list
    return (res_dict, code, msg)