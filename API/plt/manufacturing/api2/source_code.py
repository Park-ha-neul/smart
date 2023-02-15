import sqlite3
import collections
conn = sqlite3.connect('/data/aip/activate/manufacturing/api2/list.db', check_same_thread=False)

# 커서 바인딩
c = conn.cursor()

# db 조회
def db(value, params) :
    manufacturing_list = []
    route = value['routes']
    formulation = value['formulation']
    method = value['method']
    result_list = []
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
            # bd, td를 다시 추가해야한다면 주석 해제
            # oral_select = "SELECT level, material, processing, quaility, input FROM list_oral WHERE FORMULATION LIKE '" + formulation + "%' AND METHOD LIKE '" + method + "%' ORDER BY LEVEL_INDEX"
            oral_select = "SELECT level, material, processing, quaility FROM list_oral WHERE FORMULATION LIKE '%" + formulation + "%' AND METHOD LIKE '" + method + "%' ORDER BY LEVEL_INDEX"
            c.execute(oral_select)
            manufacturing_list = c.fetchall()
            for i in range(len(manufacturing_list)):
                tuple = manufacturing_list[i]
                level = tuple[0]
                material = tuple[1]
                process = tuple[2]
                quaility = tuple[3]
                # input = tuple[4]

                # 형식 맞추기
                level_dict = {}
                list_dict = {}
                list_dict["material"] = material.replace("\n", " ")
                list_dict["processing"] = process.replace("\n", " ")
                list_dict["quaility"] = quaility.replace("\n", " ")
                # list_dict["bd"] = []
                level_dict[level] = list_dict
                result_list.append(level_dict)
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
            # bd, td를 다시 추가해야한다면 주석 해제
            # oral_select = "SELECT level, material, processing, quaility, input FROM list_parenteral WHERE FORMULATION LIKE '" + formulation + "%' AND METHOD LIKE '" + method + "%' ORDER BY LEVEL_INDEX"
            oral_select = "SELECT level, material, processing, quaility FROM list_parenteral WHERE FORMULATION LIKE '%" + formulation + "%' AND METHOD LIKE '" + method + "%' ORDER BY LEVEL_INDEX"
            c.execute(oral_select)
            manufacturing_list = c.fetchall()
            result_list = []
            for i in range(len(manufacturing_list)):
                tuple = manufacturing_list[i]
                level = tuple[0]
                material = tuple[1]
                process = tuple[2]
                quaility = tuple[3]
                # input = tuple[4]

                # 형식 맞추기
                level_dict = {}
                list_dict = {}
                list_dict["material"] = material.replace("\n", " ")
                list_dict["processing"] = process.replace("\n", " ")
                list_dict["quaility"] = quaility.replace("\n", " ")
                # list_dict["bd"] = []
                level_dict[level] = list_dict
                result_list.append(level_dict)
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
            # bd, td를 다시 추가해야한다면 주석 해제
            # oral_select = "SELECT level, material, processing, quaility, input FROM list_local WHERE FORMULATION LIKE '" + formulation + "%' AND METHOD LIKE '" + method + "%' ORDER BY LEVEL_INDEX"
            oral_select = "SELECT level, material, processing, quaility FROM list_local WHERE FORMULATION LIKE '%" + formulation + "%' AND METHOD LIKE '" + method + "%' ORDER BY LEVEL_INDEX"
            c.execute(oral_select)
            manufacturing_list = c.fetchall()
            result_list = []
            for i in range(len(manufacturing_list)):
                tuple = manufacturing_list[i]
                level = tuple[0]
                material = tuple[1]
                process = tuple[2]
                quaility = tuple[3]
                # input = tuple[4]

                # 형식 맞추기
                level_dict = {}
                list_dict = {}
                list_dict["material"] = material.replace("\n", " ")
                list_dict["processing"] = process.replace("\n", " ")
                list_dict["quaility"] = quaility.replace("\n", " ")
                # list_dict["bd"] = []
                level_dict[level] = list_dict
                result_list.append(level_dict)
            code = "000"
            msg = "success"
    # 투여경로를 잘못 입력한 경우, 투여경로는 총 3가지 중 하나를 입력할 수 있음(oral, parenteral, local)
    else:
        code = "001"
        msg = "투여경로를 잘못 입력하였습니다."

    return result_list, code, msg

# 인터페이스 정의에 따른 틀 생성
def frame(value, params):
    manufacturing_list, code, msg = db(value, params)
    res_dict = {}
    res_dict["manufacturing list"] = manufacturing_list
    return (res_dict, code, msg)