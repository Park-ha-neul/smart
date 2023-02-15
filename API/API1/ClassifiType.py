from prediction_model import execute
import tensorflow as tf
import logging
import pandas as pd
import json
import csv
import datetime
from collections import OrderedDict
import Molecule as mc
import dosage as ds
import os
import prediction_model as pm

# png 저장할 파일명에 현재 날짜와 시간을 조합하기 위해 필요한 변수
now = datetime.datetime.now()

# input으로 받은 path가 존재하지 않으면 디렉토리 생성 후 거기다가 저장
def mkdir(input_path):
    input_path = input_path
    if os.path.isdir(input_path):
        return input_path
    else:
        os.makedirs(input_path)
        return input_path


def ClassifiType(value):
    # request = input으로 받은 값 한번 확인
    request = OrderedDict()
    res_dict= OrderedDict()
    response={}
    #####################################################start input key error(reqid, type, value, path)
    if 'reqid' in value:
        input_reqid = value['reqid']
    else:
        request="{}"
        res_dict="{}"
        code="001"
        msg="reqid가 누락되었습니다."
        return(request, res_dict, code, msg)

    if 'type' in value:
        input_type = value['type']
    else:
        request="{}"
        res_dict="{}"
        code = "001"
        msg = "type이 누락되었습니다."
        return(request, res_dict, code, msg)

    if 'value' in value:
        input_value = value['value']
    else:
        request="{}"
        res_dict="{}"
        code = "001"
        msg = "value가 누락되었습니다."
        return(request, res_dict, code, msg)

    if 'path' in value:
        input_path = value['path']
        png_path=None
        png_path = value['path']
    else:
        request="{}"
        res_dict="{}"
        code = "001"
        msg = "path가 누락되었습니다."
        return(request, res_dict, code, msg)
    #####################################################end input key error

    ##################################################### start {reqid, type, value, path} value error
    if value['reqid'] == '':
        request="{}"
        res_dict="{}"
        code="002"
        msg="reqid의 value가 누락되었습니다."
        return(request, res_dict, code, msg)
    elif value['type'] == '':
        request="{}"
        res_dict="{}"
        code="002"
        msg="type의 value가 누락되었습니다."
        return(request, res_dict, code, msg)
    elif value['value'] == '':
        request="{}"
        res_dict="{}"
        code="002"
        msg="value의 value가 누락되었습니다."
        return(request, res_dict, code, msg)
    elif value['path'] == '':
        request="{}"
        res_dict="{}"
        code="002"
        msg="path의 value가 누락되었습니다."
        return(request, res_dict, code, msg)
    else:
        pass

    dict_from_csv = {}
    input_type = ""
    sdf_path = ""

    #########################################################################start path form error
    if input_path.endswith('/'):
        logging.info('succee')
    else:
        request="{}"
        res_dict="{}"
        code = "007"
        msg = "path 형식의 마지막은 /로 끝나야 합니다. 다시 한번 확인해주세요."
        return(request, res_dict, code, msg)
    #########################################################################end path form error

    # csv to dict, chemical.csv 소문자로 변경하여 파일 재생성
    with open('/data/aip/activate/chemical.csv', mode='r')as inp:
        reader = csv.reader(inp)
        dict_from_csv = {rows[1]:rows[2] for rows in reader}
        dict_from_csv_values = dict_from_csv.values()

    # type = chemical
    # if "chemical" in values:
    if value['type'] == 'chemical':
        # 화학명 대소문자 구분을 위해 대문자로 들어온경우 모두 소문자로 변경하여 값 비교
        input_value = input_value.lower()
        if input_value in dict_from_csv:
            smiles = dict_from_csv[input_value]
            properties, out_array = pm.execute(smiles) # smiles를 prediction model의 execute 함수에 넣어서 모델을 돌린 값을 반환한다.
            code = "000"
            msg = "success"
            input_type = value['type']
            request = {"reqid": input_reqid, "type": input_type, "value": input_value, "path": input_path}
            png_path = mkdir(input_path)
            res_dict = {"molecule":{"smiles": smiles, "generated Molecule":mc.SdfToPng(smiles, png_path, sdf_path)},"properties":properties,"dosage":ds.dosage(out_array),'count':ds.count(out_array)}

        else:
            request="{}"
            res_dict="{}"
            code = "004"
            msg = "해당하는 chemical name이 존재하지 않습니다. smiles string으로 입력해주세요."
            return(request, res_dict, code, msg)

    # type = smiles
    # elif "smiles" in values:
    elif value['type'] == 'smiles':
        ## error Message => smiles string이 존재하지 않을때 (chemical.csv 파일에 있는 11만개의 smiles string에 존재하지 않는 경우)
        smiles = input_value
        properties, out_array = pm.execute(smiles) # smiles를 prediction model의 execute 함수에 넣어서 모델을 돌린 값을 반환한다.
        sdf_path = "/data/aip/api/sdf/"
        code = "000"
        msg = "success"
        input_type = value['type']
        # input 값 확인 return
        request = {"reqid": input_reqid, "type": input_type, "value": input_value, "path": input_path}
        # smiles string을 sdf파일로 변환
        png_path = mkdir(input_path)
        success_result = mc.SdfToPng(smiles, png_path, sdf_path)

        res_dict = {"molecule":{"smiles": smiles, "generated Molecule":mc.SdfToPng(smiles, png_path, sdf_path)},"properties":properties,"dosage":ds.dosage(out_array),'count':ds.count(out_array)}
         # properties 키 값에 properties 변수 대입한다.
    #############
    # type = sdf#
    #############
    # elif "sdf" in values:
    elif value['type'] == 'sdf':
        ########################################################################## start file형식이 sdf가 아닌 경우
        if input_value.endswith('.sdf'):
            sdf_path = input_value
        else :
            request="{}"
            res_dict="{}"
            code = "006"
            msg = "file형식이 sdf가 아닙니다. 다시 한번 확인해주세요."
            return(request, res_dict, code, msg)
        ########################################################################## end file형식이 sdf가 아닌 경우
        smiles = mc.SdfToSmiles(sdf_path)
        properties, out_array = pm.execute(smiles) # smiles를 prediction model의 execute 함수에 넣어서 모델을 돌린 값을 반환한다.
        # smiles to chemical name
        #chemical = mc.SmilesToChemical(smiles)
        code = "000"
        msg = "success"
        input_type = value['type']
        request = {"reqid": input_reqid, "type": input_type, "value": input_value, "path": input_path}
        png_path = mkdir(input_path)
        logging.info('#############png_path : %s', png_path)
        res_dict = {"molecule":{"smiles": smiles, "generated Molecule":mc.SdfToPng(smiles, png_path, sdf_path)},"properties":properties,"dosage":ds.dosage(out_array),'count':ds.count(out_array)}
        # properties 키 값에 properties 변수 대입한다.

        success_result = mc.SdfToPng2(png_path, sdf_path)
    else:
        request="{}"
        res_dict="{}"
        code = "003"
        msg = "type은 chemical, smiles, sdf중 하나를 입력해주세요."
        return(request, res_dict, code, msg)
        
    return(request, res_dict, code, msg)