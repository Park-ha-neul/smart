import sqlite3
import functools
import operator
import os
import shutil
conn = sqlite3.connect('/data/aip/activate/manufacturing/api1/method.db', check_same_thread=False)

# 커서 바인딩
c = conn.cursor()

# db 조회
def db(value) :
    manufacturing_list = []
    route = value['route']
    formulation = value['formulation']
    method = value['method']
    cqas_list = []
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
            oral_select = "SELECT LOWER(cqas) FROM cqas_oral WHERE FORMULATION LIKE '%" + formulation + "%' AND METHOD LIKE '" + method + "%'"
            c.execute(oral_select)
            # db 조회 결과값을 tuple -> string으로 변환
            tuple_to_str = functools.reduce(operator.add, (c.fetchall()[0]))
            cqas_list.append(tuple_to_str)
            if cqas_list == []:
                code = "009"
                msg = "제조공정을 다시 확인해주세요."
                return cqas_list, code, msg
            else:
                pass
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
            parenteral_select = "SELECT LOWER(cqas) FROM cqas_parenteral WHERE FORMULATION LIKE '%" + formulation + "%' AND METHOD LIKE '" + method + "%'"
            c.execute(parenteral_select)
            # db 조회 결과값을 tuple -> string으로 변환
            tuple_to_str = functools.reduce(operator.add, (c.fetchall()[0]))
            cqas_list.append(tuple_to_str)
            if cqas_list == []:
                code = "009"
                msg = "제조공정을 다시 확인해주세요."
                return cqas_list, code, msg
            else:
                pass
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
            local_select = "SELECT LOWER(cqas) FROM cqas_local WHERE FORMULATION LIKE '%" + formulation + "%' AND METHOD LIKE '" + method + "%'"
            c.execute(local_select)
            # db 조회 결과값을 tuple -> string으로 변환
            tuple_to_str = functools.reduce(operator.add, (c.fetchall()[0]))
            cqas_list.append(tuple_to_str)
            if cqas_list == []:
                code = "009"
                msg = "제조공정을 다시 확인해주세요."
                return cqas_list, code, msg
            else:
                pass
            code = "000"
            msg = "success"
        # 투여경로를 잘못 입력한 경우, 투여경로는 총 3가지 중 하나를 입력할 수 있음(oral, parenteral, local)
    else:
        code = "001"
        msg = "투여경로를 잘못 입력하였습니다."

    return cqas_list, code, msg

# 인터페이스 정의에 따른 틀 생성
def frame(value, params):
    cqas_list, code, msg = db(value)
    res_dict = {}
    res_dict["cqas_list"] = cqas_list
    return (res_dict, code, msg)