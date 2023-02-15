import datetime
from config import errors
import logging
import os
from response.Molecule import smiles2png# 0404

# png 저장할 파일명에 현재 날짜와 시간을 조합하기 위해 필요한 변수
now = datetime.datetime.now()


# input으로 받은 path가 존재하지 않으면 디렉토리 생성 후 거기다가 저장
def mkdir(path):
    if os.path.isdir(path):
        return path
    else:
        os.makedirs(path)
        return path


def req_res_and_errors(value, smiles, pred_dict):
    # input
    input_reqid = value['reqid']
    input_type = value['type']
    input_value = value['value']
    input_path = value['path']

    # 예측값
    res = pred_dict['res']
    dsg = pred_dict['dsg']
    cnt = pred_dict['cnt']

    request = {"reqid": input_reqid, "type": input_type, "value": input_value, "path": input_path}
    png_path = mkdir(input_path)  # 분자그림

    # 0404 smiles>sdf>png 는 아예 빼고 smiles>png로 바꿈. 애초에 smiles로 바꾸기 때문에.
    if input_type in ['chemical', 'smiles', 'sdf']:
        res_dict = {"molecule": {"smiles": smiles, "generated Molecule": smiles2png(smiles, png_path)}, # 0404
                    "properties": res, "dosage": dsg, 'count': cnt}
        return request, res_dict

    else:
        return 'error 003'  # 셋 중에 하나는 입력하시오


def request_error(value):
    """
    :param value: {reqid: transaction id, api가 잘 전송됐는지 확인하는 역할,

                   type : chemical, smiles, sdf (셋 중 하나)

                   value : ex) Caffeine, CCO, path/to/sdf/file

                   path : 분자그림 저장 경로

    :return: input_reqid, input_type, input_value, input_path 혹은 에러 처리

    001 {reqid/type/value/path}가 누락,

    002 key {reqid/type/value/path}의 value가 누락,

    007, path 형식의 마지막은 '/'로 끝나야 합니다
    """

    global input_reqid, input_path, input_value, input_type

    # error 처리 8가지 종류
    # 1) input key error(reqid, type, value, path)

    if 'reqid' in value:
        input_reqid = value['reqid']
    else:
        errors('001', 'requid')

    if 'type' in value:
        input_type = value['type']
    else:
        errors('001', 'type')

    if 'value' in value:
        input_value = value['value']
    else:
        errors('001', 'value')

    if 'path' in value:
        input_path = value['path']
    else:
        errors('001', 'path')

    # 2) input value error (reqid, type, value, path)

    if value['reqid'] == '':
        errors('002', 'requid')

    elif value['type'] == '':
        errors('002', 'type')

    elif value['value'] == '':
        errors('002', 'value')

    elif value['path'] == '':
        errors('002', 'path')

    else:
        pass

    # 3) path form error
    if input_path.endswith('/'):
        logging.info('success')
    else:
        return errors('007')

    return input_reqid, input_type, input_value, input_path

