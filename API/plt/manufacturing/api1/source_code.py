import sqlite3
import collections
conn = sqlite3.connect('/data/aip/activate/manufacturing/api1/method.db', check_same_thread=False)

# 커서 바인딩
c = conn.cursor()

###############################
# * 필요한 데이터
# 1. solubility
# 2. logP
# 3. bioavailable
# 4. molecular weight
################################

###############
# 예외처리 함수 #
###############
# 1. properties중에서 필요로하는 properties가 존재하지 않는 경우 예외처리 (pH Mass Solubility, logP, Bioavailability, Molecular weight)
def except_function(value):
    code = ""
    msg = ""
    value_key = list(value.keys())
    properties = value['properties']
    properties_key = list(properties.keys())

    # 2. primary key error
    if 'primary' in value_key:
        pass
    else:
        code = "004"
        msg = "primary key 값을 확인해주세요."

    if 'pH Mass Solubility' in properties_key:
        # 그 다음 속성 비교 (logP)
        if 'logP' in properties_key:
            # 그 다음 속성 비교(Bioavailability)
            if 'Bioavailability' in properties_key:
                # 그 다음 속성 비교(Molecular weight(g/mol))
                if 'Molecular weight(g/mol)' in properties_key:
                    pass
                else:
                    code = "002"
                    msg = "Molecular weight(g/mol) key 값을 확인해주세요."
            else:
                code = "002"
                msg = "Bioavailability key 값을 확인해주세요."
        else:
            code = "002"
            msg = "logP key 값을 확인해주세요."
    else:
        code = "002"
        msg = "pH Mass Solubility key 값을 확인해주세요."

    return code, msg

# list 안 중복 체크 (중복되는게 없다면, 2개 이상의 조건을 만족하는 데이터가 없다는 이야기, 이러한 경우 예외처리 진행)
def has_duplicates(properties_list):
    return len(properties_list) != len(set(properties_list))

# db 조회
def db(value, params) :
    method_list = []
    # 예외처리 함수
    code, msg = except_function(value)
    if len(code) > 0:
        pass
    else:
        route = value['routes']
        formulation = value['formulation']
        method_list = []
        # DB 데이터와 비교하기 위해서는 float 형식이 아닌 str 형식이여야함 데이터 변환 과정 진행 (float -> str)
        solubility3 = str(value['properties']['pH Mass Solubility'][2])
        solubility7 = str(value['properties']['pH Mass Solubility'][6])
        logP = str(value['properties']['logP'])
        bio = str(value['properties']['Bioavailability'][1])
        mw = str(value['properties']['Molecular weight(g/mol)'])
        volume = str(round(value['primary']/value['properties']['Molecular weight(g/mol)']*100, 2))

        # 예외처리 2. 각 테이블에 존재하지 않는 formulation이 들어온 경우
        db_formulation_list = ['Capsule, Oral Capsule', 'Tablet', 'Oral Suspension, Suspension', 'Oral Solution', 'Granule', 'Powder', 'Gel', 'Sublingual', 'Sublingual Spray', 'Bucal Tablet, Bucal Film', 'Injection', 'Intramuscular injection', 'Intravenous Injection', 'Implant', 'Injection Suspension', 'Aerosol', 'Transmucosal Lozenge', 'Topical Suspension, Topical Solution', 'Cream, Topical Cream', 'Emusion', 'Gel, Topical Gel', 'Lotion', 'Ointment', 'Patch', 'Shampoo, Topical Shampoo', 'Nasal Spray, Nasal Solution', 'Spray', 'Ophthalmic Gel', 'Ophthalmic Solution', 'Ophthalmic Suspension, Ophthalmic Emulsion', 'Intravitreal Implant', 'Suppository', 'Suspension', 'Vaginal', 'Urethral Suppository']
        if formulation not in db_formulation_list:
            code = "003"
            msg = "제형을 잘못 입력하였습니다."
        else:
            ################################################################################################################
            # step1. 각각 properties 를 조건에 맞게 DB 조회 (입력받은 투여경로 일치 및 속성과 일치하면 properties_list 에 데이터 넣기) #
            ################################################################################################################
            properties_list = []
            if route == 'oral':
                oral_formulation_list = ['Capsule, Oral Capsule', 'Tablet', 'Oral Suspension, Suspension', 'Oral Solution', 'Granule', 'Powder', 'Gel', 'Sublingual', 'Sublingual Spray', 'Bucal Tablet, Bucal Film']
                if formulation not in oral_formulation_list:
                    code = "005"
                    msg = "투여경로에 맞는 제형을 입력해주세요."
                else:
                    # 1. ph3_solubility
                    oral_ph3 = "SELECT METHOD FROM ORAL WHERE FORMULATION LIKE '%" + formulation  + "%' AND PH3_SOL_MIN < " + solubility3 + " AND " + solubility3 + "< PH3_SOL_MAX"
                    c.execute(oral_ph3)
                    properties_list = properties_list + [item[0] for item in c.fetchall()]
                    # 2. ph7_solubility
                    oral_ph7 = "SELECT METHOD FROM ORAL WHERE FORMULATION LIKE '%" + formulation  + "%' AND PH7_SOL_MIN < " + solubility7 + " AND " + solubility7 + "< PH7_SOL_MAX"
                    c.execute(oral_ph7)
                    properties_list = properties_list + [item[0] for item in c.fetchall()]
                    # 3. volume
                    oral_volume = "SELECT METHOD FROM ORAL WHERE FORMULATION LIKE '%" + formulation  + "%' AND VOLUME_MIN < " + volume + " AND " + volume + "< VOLUME_MAX"
                    c.execute(oral_volume)
                    properties_list = properties_list + [item[0] for item in c.fetchall()]
                    # 4. logp
                    oral_logP = "SELECT METHOD FROM ORAL WHERE FORMULATION LIKE '%" + formulation  + "%' AND LOGP_MIN < " + logP + " AND " + logP + "< LOGP_MAX"
                    c.execute(oral_logP)
                    properties_list = properties_list + [item[0] for item in c.fetchall()]
                    # 5. bio
                    oral_bio = "SELECT METHOD FROM ORAL WHERE FORMULATION LIKE '%" + formulation  + "%' AND BIO_MIN < " + bio + " AND " + bio + "< BIO_MAX"
                    c.execute(oral_bio)
                    properties_list = properties_list + [item[0] for item in c.fetchall()]
                    # 6. mw
                    oral_mw = "SELECT METHOD FROM ORAL WHERE FORMULATION LIKE '%" + formulation  + "%' AND MOL_WEIGHT_MIN < " + mw + " AND " + mw + "< MOL_WEIGHT_MAX"
                    c.execute(oral_mw)
                    properties_list = properties_list + [item[0] for item in c.fetchall()]
                    duplicate_check = has_duplicates(properties_list)
                    if duplicate_check == False:
                        code = "006"
                        msg = "조건에 맞는 제조방법이 없습니다. 다른 제형을 선택해주세요."
                    else:
                        code = "000"
                        msg = "success"

            elif route == 'parenteral':
                # 1. ph3_solubility
                parenteral_formulation_list = ['Injection', 'Intramuscular injection', 'Intravenous Injection', 'Implant', 'Injection Suspension']
                if formulation not in parenteral_formulation_list:
                    code = "005"
                    msg = "투여경로에 맞는 제형을 입력해주세요."
                else:
                    parenteral_ph3 = "SELECT METHOD FROM PARENTERAL WHERE FORMULATION LIKE '%" + formulation  + "%' AND PH3_SOL_MIN < " + solubility3 + " AND " + solubility3 + "< PH3_SOL_MAX"
                    c.execute(parenteral_ph3)
                    properties_list = properties_list + [item[0] for item in c.fetchall()]
                    # 2. ph7_solubility
                    parenteral_ph7 = "SELECT METHOD FROM PARENTERAL WHERE FORMULATION LIKE '%" + formulation  + "%' AND PH7_SOL_MIN < " + solubility7 + " AND " + solubility7 + "< PH7_SOL_MAX"
                    c.execute(parenteral_ph7)
                    properties_list = properties_list + [item[0] for item in c.fetchall()]
                    # 3. volume
                    parenteral_volume = "SELECT METHOD FROM PARENTERAL WHERE FORMULATION LIKE '%" + formulation  + "%' AND VOLUME_MIN < " + volume + " AND " + volume + "< VOLUME_MAX"
                    c.execute(parenteral_volume)
                    properties_list = properties_list + [item[0] for item in c.fetchall()]
                    # 4. logp
                    parenteral_logP = "SELECT METHOD FROM PARENTERAL WHERE FORMULATION LIKE '%" + formulation  + "%' AND LOGP_MIN < " + logP + " AND " + logP + "< LOGP_MAX"
                    c.execute(parenteral_logP)
                    properties_list = properties_list + [item[0] for item in c.fetchall()]
                    # 5. bio
                    parenteral_bio = "SELECT METHOD FROM PARENTERAL WHERE FORMULATION LIKE '%" + formulation  + "%' AND BIO_MIN < " + bio + " AND " + bio + "< BIO_MAX"
                    c.execute(parenteral_bio)
                    properties_list = properties_list + [item[0] for item in c.fetchall()]
                    # 6. mw
                    parenteral_mw = "SELECT METHOD FROM PARENTERAL WHERE FORMULATION LIKE '%" + formulation  + "%' AND MOL_WEIGHT_MIN < " + mw + " AND " + mw + "< MOL_WEIGHT_MAX"
                    c.execute(parenteral_mw)
                    properties_list = properties_list + [item[0] for item in c.fetchall()]
                    duplicate_check = has_duplicates(properties_list)
                    if duplicate_check == False:
                        code = "006"
                        msg = "조건에 맞는 제조방법이 없습니다. 다른 제형을 선택해주세요."
                    else:
                        code = "000"
                        msg = "success"

            elif route == 'local':
                # 1. ph3_solubility
                local_formulation_list = ['Aerosol', 'Transmucosal Lozenge', 'Topical Suspension, Topical Solution', 'Cream, Topical Cream', 'Emusion', 'Gel, Topical Gel', 'Lotion', 'Ointment', 'Patch', 'Shampoo, Topical Shampoo', 'Nasal Spray, Nasal Solution', 'Spray', 'Ophthalmic Gel', 'Ophthalmic Solution', 'Ophthalmic Suspension, Ophthalmic Emulsion', 'Intravitreal Implant', 'Suppository', 'Suspension', 'Vaginal', 'Urethral Suppository']
                if formulation not in local_formulation_list:
                    code = "005"
                    msg = "투여경로에 맞는 제형을 입력해주세요."
                else:
                    local_ph3 = "SELECT METHOD FROM LOCAL WHERE FORMULATION LIKE '%" + formulation  + "%' AND PH3_SOL_MIN < " + solubility3 + " AND " + solubility3 + "< PH3_SOL_MAX"
                    c.execute(local_ph3)
                    properties_list = properties_list + [item[0] for item in c.fetchall()]
                    # 2. ph7_solubility
                    local_ph7 = "SELECT METHOD FROM LOCAL WHERE FORMULATION LIKE '%" + formulation  + "%' AND PH7_SOL_MIN < " + solubility7 + " AND " + solubility7 + "< PH7_SOL_MAX"
                    c.execute(local_ph7)
                    properties_list = properties_list + [item[0] for item in c.fetchall()]
                    # 3. volume
                    local_volume = "SELECT METHOD FROM LOCAL WHERE FORMULATION LIKE '%" + formulation  + "%' AND VOLUME_MIN < " + volume + " AND " + volume + "< VOLUME_MAX"
                    c.execute(local_volume)
                    properties_list = properties_list + [item[0] for item in c.fetchall()]
                    # 4. logp
                    local_logP = "SELECT METHOD FROM LOCAL WHERE FORMULATION LIKE '%" + formulation  + "%' AND LOGP_MIN < " + logP + " AND " + logP + "< LOGP_MAX"
                    c.execute(local_logP)
                    properties_list = properties_list + [item[0] for item in c.fetchall()]
                    # 5. bio
                    local_bio = "SELECT METHOD FROM LOCAL WHERE FORMULATION LIKE '%" + formulation  + "%' AND BIO_MIN < " + bio + " AND " + bio + "< BIO_MAX"
                    c.execute(local_bio)
                    properties_list = properties_list + [item[0] for item in c.fetchall()]
                    # 6. mw
                    local_mw = "SELECT METHOD FROM LOCAL WHERE FORMULATION LIKE '%" + formulation  + "%' AND MOL_WEIGHT_MIN < " + mw + " AND " + mw + "< MOL_WEIGHT_MAX"
                    c.execute(local_mw)
                    properties_list = properties_list + [item[0] for item in c.fetchall()]
                    duplicate_check = has_duplicates(properties_list)
                    if duplicate_check == False:
                        code = "006"
                        msg = "조건에 맞는 제조방법이 없습니다. 다른 제형을 선택해주세요."
                    else:
                        code = "000"
                        msg = "success"
            # 투여경로를 잘못 입력한 경우, 투여경로는 총 3가지 중 하나를 입력할 수 있음(oral, parenteral, local)
            else:
                code = "001"
                msg = "투여경로를 잘못 입력하였습니다."

            #################################################
            # step2. list 안에 일치하는 요소 비교해서 count 세기 #
            #################################################
            # 1. collections 모듈을 사용해서, 리스트 요소별 개수 구하기
            cnt_dict = {}
            cnt_dict=collections.Counter(properties_list)

            # 2. cnt_dict 의 values 만 추출해서 values 값이 2이상인 key 추출
            values = list(cnt_dict.values())
            keys = list(cnt_dict.keys())
            for i in range(len(values)):
                if values[i] > 1:
                    method_list.append(keys[i])

    return method_list, code, msg

# 인터페이스 정의에 따른 틀 생성
def frame(value, params):
    method_list, code, msg = db(value, params)
    res_dict = {}
    if len(method_list) == 0:
        pass
    else:
        res_dict["method"] = method_list
    return (res_dict, code, msg)