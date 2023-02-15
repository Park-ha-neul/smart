import os
import shutil
import logging
from ast import literal_eval  # 0405

logging.basicConfig(level=logging.INFO)


def process_for_train(pm):
    mode = 'train'
    _do_preprocess(pm, mode)


def process_for_test(pm):
    """테스트용 데이터 전처리
    """
    mode = 'test'
    _do_preprocess(pm, mode)


def init_svc(im, rule):
    """추론 서비스 초기화
    """
    meta_path = im.meta_path
    return {"meta_path": meta_path, "rule": rule}


def transform(df, params, batch_id):
    """
    추론 데이터로 변환
    :param df: str({'reqid': 'dd', 'type': 'chemical', 'value': 'CLOBETASOL PROPIONATE', 'path': '/data/test/'})
    :param params: 플랫폼 전처리 모델 파라미터
    :param batch_id:  플랫폼 형식에 맞춰서 넣어준 arg
    :return: str(dict) to dict
    """
    # dic_df = literal_eval(df) # 0405

    # return dic_df # 0405
    return df  # 0405_16
    # transform 에서 df로 받아준 후 inference()에서 df에서 값만 빼오기.


def _do_preprocess(pm, mode):
    # data 원본 경로
    source_path = pm.source_path
    # data 전처리 완료된 경로
    target_path = pm.target_path

    record_list = os.listdir(source_path)
    target_list = os.listdir(target_path)
    #
    # logging.info(f'record list {record_list}')
    # logging.info(f'target_list {target_list}')
    # logging.info(f'target_path {target_path}')

    for record in record_list:
        shutil.copy2(os.path.join(source_path, record), os.path.join(target_path, record))
