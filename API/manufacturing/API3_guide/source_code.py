import sqlite3
import collections
# conn = sqlite3.connect('D:/project/2021/스마트제형설계/제조공정/api3/guide.db', check_same_thread=False)
conn = sqlite3.connect('/data/aip/activate/manufacturing/api3/guide.db', check_same_thread=False)

# 커서 바인딩
c = conn.cursor()

# db 조회
def db(value) :
    manufacturing_list = []
    route = value['routes']
    formulation = value['formulation']
    method = value['method']
    result_list = []
    guide_dict = {}
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
            oral_select = "SELECT process, cqas, degree FROM guide_oral WHERE FORMULATION LIKE '%" + formulation + "%' AND METHOD LIKE '" + method + "%' ORDER BY process_id"
            c.execute(oral_select)
            manufacturing_list = c.fetchall()
            # db 에서 process 명만 level_list 에 저장
            level_list = []
            # db 에서 cqas 명만 process_list 에 저장
            process_list = []
            # db 에서 cqas 와 degree 를 dict 형식으로 변환하여 process_dict 에 저장
            process_dict = {}

            level = []
            for i in range(len(manufacturing_list)):
                tuple2li = list(manufacturing_list[i])
                # level, cqas, degree 를 db 에서 추출
                level.append(tuple2li[0])
                cqas = tuple2li[1]
                degree = tuple2li[2]
                process_dict = {}
                process_dict[cqas] = degree

                if i > 0 :
                    # level 아래 값 추가
                    if level[i-1] == level[i]:
                        guide_dict[level[i]].append(process_dict)
                    # level 추가
                    else:
                        guide_dict[level[i]] = [process_dict]
                else :
                    guide_dict[level[i]] = [process_dict]
            code = "000"
            msg = "success"

    elif route == 'parenteral':
        print('here')
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
            oral_select = "SELECT process, cqas, degree FROM guide_parenteral WHERE FORMULATION LIKE '%" + formulation + "%' AND METHOD LIKE '" + method + "%' ORDER BY process_id"
            c.execute(oral_select)
            manufacturing_list = c.fetchall()
            # db 에서 process 명만 level_list 에 저장
            level_list = []
            # db 에서 cqas 명만 process_list 에 저장
            process_list = []
            # db 에서 cqas 와 degree 를 dict 형식으로 변환하여 process_dict 에 저장
            process_dict = {}

            level = []
            for i in range(len(manufacturing_list)):
                tuple2li = list(manufacturing_list[i])
                # level, cqas, degree 를 db 에서 추출
                level.append(tuple2li[0])
                cqas = tuple2li[1]
                degree = tuple2li[2]
                process_dict = {}
                process_dict[cqas] = degree

                if i > 0 :
                    # level 아래 값 추가
                    if level[i-1] == level[i]:
                        guide_dict[level[i]].append(process_dict)
                    # level 추가
                    else:
                        guide_dict[level[i]] = [process_dict]
                else :
                    guide_dict[level[i]] = [process_dict]
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
            oral_select = "SELECT process, cqas, degree FROM guide_local WHERE FORMULATION LIKE '%" + formulation + "%' AND METHOD LIKE '" + method + "%' ORDER BY process_id"
            c.execute(oral_select)
            manufacturing_list = c.fetchall()
            # db 에서 process 명만 level_list 에 저장
            level_list = []
            # db 에서 cqas 명만 process_list 에 저장
            process_list = []
            # db 에서 cqas 와 degree 를 dict 형식으로 변환하여 process_dict 에 저장
            process_dict = {}

            level = []
            for i in range(len(manufacturing_list)):
                tuple2li = list(manufacturing_list[i])
                # level, cqas, degree 를 db 에서 추출
                level.append(tuple2li[0])
                cqas = tuple2li[1]
                degree = tuple2li[2]
                process_dict = {}
                process_dict[cqas] = degree

                if i > 0 :
                    # level 아래 값 추가
                    if level[i-1] == level[i]:
                        guide_dict[level[i]].append(process_dict)
                    # level 추가
                    else:
                        guide_dict[level[i]] = [process_dict]
                else :
                    guide_dict[level[i]] = [process_dict]
            code = "000"
            msg = "success"
    # 투여경로를 잘못 입력한 경우, 투여경로는 총 3가지 중 하나를 입력할 수 있음(oral, parenteral, local)
    else:
        code = "001"
        msg = "투여경로를 잘못 입력하였습니다."

    return guide_dict, code, msg

def pha(value):
    level = value['level']
    cqa_list = []

    # pha 가이드에 나오는 cqa 를
    guide_dict, code, msg = db(value)
    dict_value_list = list(guide_dict.values())[0]
    for i in range(len(dict_value_list)):
        cqa_list += list(dict_value_list[i].keys())
    cqas_dict = {}
    cpp_dict = {}
    cqas_dict["cqas"] = cqa_list
    cpp_dict["cpp"] = level
    pha_list = []
    pha_list.append(cqas_dict)
    pha_list.append(cpp_dict)

    return pha_list
# 인터페이스 정의에 따른 틀 생성

def frame(value, params):
    guide_dict, code, msg = db(value)
    if guide_dict == {}:
        pha_list = []
    else:
        pha_list = pha(value)
    res_dict = {}
    res_dict["guide"] = guide_dict
    res_dict["pha"] = pha_list
    return (res_dict, code, msg)