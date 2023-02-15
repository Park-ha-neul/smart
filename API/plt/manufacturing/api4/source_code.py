import sqlite3
import functools
import operator
import os
import shutil

conn = sqlite3.connect('/data/aip/activate/manufacturing/api4/fmea.db', check_same_thread=False)
conn_process = sqlite3.connect('/data/aip/activate/manufacturing/api4/process.db', check_same_thread=False)

# 커서 바인딩
c = conn.cursor()
process = conn_process.cursor()

# 존재하지 않는 path인 경우 생성해주는 code
def mkdir(input_path) :
    if os.path.isdir(input_path):
        return input_path
    else:
        os.makedirs(input_path)
        return input_path

def pha_compare(pha):
    check_list = []
    check_key_list = []
    for i in range(len(pha)):
        content_dict = pha[i]
        content_dict_value = list(content_dict.values())
        check_dict = {}
        if '높음' in content_dict_value[0]:
            for key in content_dict.keys():
                check_key_list.append(key)
                check_dict[key] = "o"
                check_list.append(check_dict)
        else:
            for keys in content_dict.keys():
                check_dict[keys] = "x"
                check_list.append(check_dict)
    return check_list, check_key_list

# db 조회
def db(value) :
    manufacturing_list = []
    route = value['routes']
    formulation = value['formulation']
    method = value['method']
    level = value['level']
    pha = value['pha']
    result_list = []
    process_list = []
    cpp_dict_list = []
    if 'path' in value:
        input_path = value['path']
        input_path = mkdir(input_path)
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
                    ## 1. fmea 테이블 DB 조회(입력한 제형, 제조방법, 제조공정에 따른 cpp, failure, effect 출력)
                    oral_select = "SELECT level, cpps, failure, effect FROM fmea_oral WHERE FORMULATION LIKE '%" + formulation + "%' AND METHOD LIKE '" + method + "%' AND LEVEL LIKE '" + level[i] +"%' ORDER BY id"
                    c.execute(oral_select)
                    manufacturing_list.append(c.fetchall())
                    # list 에서 빈 요소 제거
                    manufacturing_list = list(filter(None, manufacturing_list))
                    if manufacturing_list == []:
                        code = "009"
                        msg = "제조공정을 다시 확인해주세요."
                        return process_list, cpp_dict_list, code, msg
                    else:
                        pass
                    ##################################################################################
                    ## 2. 유닛 공정 이미지 목록 DB 조회 (입력한 제형, 제조방법, 제조공정에 따른 cpp와 risk 출력)#
                    ##################################################################################
                    # 아래와 같은 형식으로 출력하기 위함, {"중복되는 cpp 명" : [여러개의 risk]}
                    # step1. db 조회 "Risks" : {"Sifting" : ["Uneven particle distribution", "테스트", "테스트2", "테스트3"],"Mill type" : ["테스트1", "테스트2", "테스트3", "테스트4"]}
                    oral_process_select = "SELECT cpps, risk FROM process_oral WHERE FORMULATION LIKE '%" + formulation + "%' AND METHOD LIKE '" + method + "%' AND LEVEL LIKE '" + level[i] +"%' ORDER BY cpp_id"
                    process.execute(oral_process_select)
                    process_db_result = process.fetchall()
                ######################
                # 2. process_img 출력 #
                ######################
                # step2. cpp_list, risk, unit 출력, db 조회된 결과값의 앞에 항목을 key 로 뒤에를 value 로 선정하고 key 와 중복되면 나머지 value 들이 list 로 나오게끔 출력
                    # 제조공정 중 '과립'은 제조방법이 습식과립인지, 건식과립인지에 따라 이미지가 달라짐
                    if method == '습식과립' and level[i] == '과립':
                        save_path_list = []
                        img_path = '/data/aip/activate/manufacturing/api4/img/과립(습식).jpg'
                        shutil.copy(img_path, input_path)
                        path = img_path.split('/')
                        filename = path[-1]
                        save_path = input_path + filename
                        save_path_list.append(save_path)
                    elif method == '건식과립' and level[i] == '과립':
                        save_path_list = []
                        img_path = '/data/aip/activate/manufacturing/api4/img/과립(건식).jpg'
                        shutil.copy(img_path, input_path)
                        path = img_path.split('/')
                        filename = path[-1]
                        save_path = input_path + filename
                        save_path_list.append(save_path)
                    else:
                        oral_process_img = "SELECT img_path FROM process_img WHERE level LIKE '" + level[i] + "%'"
                        process.execute(oral_process_img)
                        img_db_result_list = process.fetchall()
                        if len(img_db_result_list)==2:
                            for path in img_db_result_list:
                                img_path = path
                        else:
                            img_path = img_db_result_list[0]
                        item_img_path = img_path[0].split(',')

                        save_path_list = []
                        for j in range(len(item_img_path)):
                            img_path = item_img_path[j]
                            shutil.copy(img_path, input_path)
                            path = img_path.split('/')
                            filename = path[-1]
                            save_path = input_path + filename
                            save_path_list.append(save_path)
                    process_dict = {}
                    risk_result = {}
                    for key, value in process_db_result:
                        risk_result.setdefault(key, []).append(value)
                    cpp_list = list(risk_result.keys())
                    process_dict['png'] = save_path_list
                    process_dict['unit'] = level[i]
                    process_dict['risk'] = risk_result
                    process_dict['cpp'] = cpp_list
                    process_list.append(process_dict)
                ############################################
                # 입력된 '높음', '낮음'에 대한 색 표시를 위한 코드#
                ############################################
                check_list, check_key_list = pha_compare(pha)
                ###############################################
                # risk 로 표시된 항목에 대한 유닛/공정 이미지만 출력 #
                ###############################################
                process_result_list = []
                if check_key_list == []:
                    process_result_list = []
                else:
                    for k in process_list:
                        if k['unit'] in check_key_list:
                            process_result_list.append(k)
                        else:
                            pass
                ###############
                # 1. fmea 출력 #
                ###############
                unit = []
                cpps = []
                failure = []
                effect = []
                for i in range(len(manufacturing_list)):
                    tuple2li = list(manufacturing_list[i])
                    for j in range(len(tuple2li)):
                        unit.append(tuple2li[j][0])
                        cpps.append(tuple2li[j][1])
                        failure.append(tuple2li[j][2])
                        effect.append(tuple2li[j][3])

                cpp_dict = {}
                for z in range(len(unit)):
                    cpp_dict['unit'] = unit[z]
                    cpp_dict['cpps'] = cpps[z]
                    cpp_dict['failure'] = failure[z]
                    cpp_dict['effect'] = effect[z]
                    cpp_dict_copy = cpp_dict.copy()
                    cpp_dict_list.append(cpp_dict_copy)
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
                    ## 1. fmea 테이블 DB 조회(입력한 제형, 제조방법, 제조공정에 따른 cpp, failure, effect 출력)
                    parenteral_select = "SELECT level, cpps, failure, effect FROM fmea_parenteral WHERE FORMULATION LIKE '%" + formulation + "%' AND METHOD LIKE '" + method + "%' AND LEVEL LIKE '" + level[i] +"%' ORDER BY id"
                    c.execute(parenteral_select)
                    manufacturing_list.append(c.fetchall())
                    # list 에서 빈 요소 제거
                    manufacturing_list = list(filter(None, manufacturing_list))
                    if manufacturing_list == []:
                        code = "009"
                        msg = "제조공정을 다시 확인해주세요."
                        return process_list, cpp_dict_list, code, msg
                    else:
                        pass
                    ##################################################################################
                    ## 2. 유닛 공정 이미지 목록 DB 조회 (입력한 제형, 제조방법, 제조공정에 따른 cpp와 risk 출력)#
                    ##################################################################################
                    # 아래와 같은 형식으로 출력하기 위함, {"중복되는 cpp 명" : [여러개의 risk]}
                    # step1. db 조회 "Risks" : {"Sifting" : ["Uneven particle distribution", "테스트", "테스트2", "테스트3"],"Mill type" : ["테스트1", "테스트2", "테스트3", "테스트4"]}
                    parenteral_process_select = "SELECT cpps, risk FROM process_parenteral WHERE FORMULATION LIKE '%" + formulation + "%' AND METHOD LIKE '" + method + "%' AND LEVEL LIKE '" + level[i] +"%' ORDER BY cpp_id"
                    process.execute(parenteral_process_select)
                    process_db_result = process.fetchall()

                ######################
                # 2. process_img 출력 #
                ######################
                # step2. cpp_list, risk, unit 출력, db 조회된 결과값의 앞에 항목을 key 로 뒤에를 value 로 선정하고 key 와 중복되면 나머지 value 들이 list 로 나오게끔 출력
                    # step3. 이미지 데이터 추출
                    parenteral_process_img = "SELECT img_path FROM process_img WHERE level LIKE '" + level[i] + "%'"
                    process.execute(parenteral_process_img)
                    img_db_result_list = process.fetchall()
                    if len(img_db_result_list)==2:
                        for path in img_db_result_list:
                            img_path = path
                    else:
                        img_path = img_db_result_list[0]
                    item_img_path = img_path[0].split(',')

                    save_path_list = []
                    for i in range(len(item_img_path)):
                        img_path = item_img_path[i]
                        shutil.copy(img_path, input_path)
                        path = img_path.split('/')
                        filename = path[-1]
                        save_path = input_path + filename
                        save_path_list.append(save_path)
                    process_dict = {}
                    risk_result = {}
                    for key, value in process_db_result:
                        risk_result.setdefault(key, []).append(value)
                    cpp_list = list(risk_result.keys())
                    process_dict['png'] = save_path_list
                    process_dict['unit'] = level[i]
                    process_dict['risk'] = risk_result
                    process_dict['cpp'] = cpp_list
                    process_list.append(process_dict)
                ############################################
                # 입력된 '높음', '낮음'에 대한 색 표시를 위한 코드#
                ############################################
                check_list, check_key_list = pha_compare(pha)
                ###############################################
                # risk 로 표시된 항목에 대한 유닛/공정 이미지만 출력 #
                ###############################################
                process_result_list = []
                if check_key_list == []:
                    process_result_list = []
                else:
                    for k in process_list:
                        if k['unit'] in check_key_list:
                            process_result_list.append(k)
                        else:
                            pass
                ###############
                # 1. fmea 출력 #
                ###############
                unit = []
                cpps = []
                failure = []
                effect = []
                for i in range(len(manufacturing_list)):
                    tuple2li = list(manufacturing_list[i])
                    for j in range(len(tuple2li)):
                        unit.append(tuple2li[j][0])
                        cpps.append(tuple2li[j][1])
                        failure.append(tuple2li[j][2])
                        effect.append(tuple2li[j][3])

                cpp_dict = {}
                for z in range(len(unit)):
                    cpp_dict['unit'] = unit[z]
                    cpp_dict['cpps'] = cpps[z]
                    cpp_dict['failure'] = failure[z]
                    cpp_dict['effect'] = effect[z]
                    cpp_dict_copy = cpp_dict.copy()
                    cpp_dict_list.append(cpp_dict_copy)
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
                    local_select = "SELECT level, cpps, failure, effect FROM fmea_local WHERE FORMULATION LIKE '%" + formulation + "%' AND METHOD LIKE '" + method + "%' AND LEVEL LIKE '" + level[i] +"%' ORDER BY id"
                    c.execute(local_select)
                    manufacturing_list.append(c.fetchall())
                    # list 에서 빈 요소 제거
                    manufacturing_list = list(filter(None, manufacturing_list))
                    if manufacturing_list == []:
                        code = "009"
                        msg = "제조공정을 다시 확인해주세요."
                        return process_list, cpp_dict_list, code, msg
                    else:
                        pass
                    ##################################################################################
                    ## 2. 유닛 공정 이미지 목록 DB 조회 (입력한 제형, 제조방법, 제조공정에 따른 cpp와 risk 출력)#
                    ##################################################################################
                    # 아래와 같은 형식으로 출력하기 위함, {"중복되는 cpp 명" : [여러개의 risk]}
                    # step1. db 조회 "Risks" : {"Sifting" : ["Uneven particle distribution", "테스트", "테스트2", "테스트3"],"Mill type" : ["테스트1", "테스트2", "테스트3", "테스트4"]}
                    local_process_select = "SELECT cpps, risk FROM process_local WHERE FORMULATION LIKE '%" + formulation + "%' AND METHOD LIKE '" + method + "%' AND LEVEL LIKE '" + level[i] +"%' ORDER BY cpp_id"
                    process.execute(local_process_select)
                    process_db_result = process.fetchall()

                ######################
                # 2. process_img 출력 #
                ######################
                # step2. cpp_list, risk, unit 출력, db 조회된 결과값의 앞에 항목을 key 로 뒤에를 value 로 선정하고 key 와 중복되면 나머지 value 들이 list 로 나오게끔 출력
                    # step3. 이미지 데이터 추출
                    local_process_img = "SELECT img_path FROM process_img WHERE level LIKE '" + level[i] + "%'"
                    process.execute(local_process_img)
                    img_db_result_list = process.fetchall()
                    if len(img_db_result_list)==2:
                        for path in img_db_result_list:
                            img_path = path
                    else:
                        img_path = img_db_result_list[0]
                    item_img_path = img_path[0].split(',')

                    save_path_list = []
                    for j in range(len(item_img_path)):
                        img_path = item_img_path[j]
                        shutil.copy(img_path, input_path)
                        path = img_path.split('/')
                        filename = path[-1]
                        save_path = input_path + filename
                        save_path_list.append(save_path)
                    process_dict = {}
                    risk_result = {}
                    for key, value in process_db_result:
                        risk_result.setdefault(key, []).append(value)
                    cpp_list = list(risk_result.keys())
                    process_dict['png'] = save_path_list
                    process_dict['unit'] = level[i]
                    process_dict['risk'] = risk_result
                    process_dict['cpp'] = cpp_list
                    process_list.append(process_dict)
                ############################################
                # 입력된 '높음', '낮음'에 대한 색 표시를 위한 코드#
                ############################################
                check_list, check_key_list = pha_compare(pha)
                ###############################################
                # risk 로 표시된 항목에 대한 유닛/공정 이미지만 출력 #
                ###############################################
                process_result_list = []
                if check_key_list == []:
                    process_result_list = []
                else:
                    for k in process_list:
                        if k['unit'] in check_key_list:
                            process_result_list.append(k)
                        else:
                            pass

                ###############
                # 1. fmea 출력 #
                ###############
                unit = []
                cpps = []
                failure = []
                effect = []
                for i in range(len(manufacturing_list)):
                    tuple2li = list(manufacturing_list[i])
                    for j in range(len(tuple2li)):
                        unit.append(tuple2li[j][0])
                        cpps.append(tuple2li[j][1])
                        failure.append(tuple2li[j][2])
                        effect.append(tuple2li[j][3])

                cpp_dict = {}
                for z in range(len(unit)):
                    cpp_dict['unit'] = unit[z]
                    cpp_dict['cpps'] = cpps[z]
                    cpp_dict['failure'] = failure[z]
                    cpp_dict['effect'] = effect[z]
                    cpp_dict_copy = cpp_dict.copy()
                    cpp_dict_list.append(cpp_dict_copy)
                code = "000"
                msg = "success"
            # 투여경로를 잘못 입력한 경우, 투여경로는 총 3가지 중 하나를 입력할 수 있음(oral, parenteral, local)
        else:
            code = "001"
            msg = "투여경로를 잘못 입력하였습니다."
    else:
        code = "008"
        msg = "path가 누락되었습니다."

    return check_list, process_list, cpp_dict_list, code, msg

# 인터페이스 정의에 따른 틀 생성
def frame(value, params):
    check_list, process_result_list, cpp_dict_list, code, msg = db(value)
    res_dict = {}
    factor_list = []
    res_dict["check_list"] = check_list
    res_dict["fmea"] = cpp_dict_list
    res_dict["process img"] = process_result_list
    return (res_dict, code, msg)