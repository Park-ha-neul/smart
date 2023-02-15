#-*- coding: utf-8 -*-
import logging
import pandas as pd
import csv
import ast
import os
# image to base64
import base64
# image name return
from pathlib import Path
# R sciprt 불러오기위한 file import
# local
from API_6_TEST.R import runScript
# 플랫폼
# import R -> 밑에 R.runScript로 바꿔야함

# R로 돌린 결과값 csv를 가져온 후 변환
# inpt으로 받는 path가 서버에 존재하지 않는 경우 path를 만들어줘야함
def mkdir(value):
    pareto_path = value['pareto-path']
    contour_path = value['contour-path']
    response_path = value['response-path']

    if os.path.isdir(pareto_path) :
        pass
    else:
        os.makedirs(pareto_path)
    if os.path.isdir(contour_path) :
        pass
    else:
        os.makedirs(contour_path)
    if os.path.isdir(response_path) :
        pass
    else:
        os.makedirs(response_path)

def get_graph_list(path):
    graph_list = []
    for i in range(len(path)):
        graph_dict = {}
        # image to base64, b'base64 string ....' -> b 접두사 제거
        with open(path[i], 'rb') as img:
            base64_string = base64.b64encode(img.read()).decode('utf-8')
        graph_dict['image_name'] = Path(path[i]).stem
        graph_dict['image_to_base64'] = base64_string
        graph_list.append(graph_dict)
    return graph_list


def R_graph(value):
    #### path 데이터 출력
    # rscript를 돌려서 나온 그래프가 저장된 경로를 csv로 받아서 list형태로 반환
    output_path, output_path2, y_list = runScript(value)
    contour = output_path['ch_contour']
    response = output_path['ch_response']
    pareto = output_path2['ch_pareto']

    contour_path_list = get_graph_list(contour)
    response_path_list = get_graph_list(response)
    pareto_path_list = get_graph_list(pareto)

    #### effects 값 출력
    ## min, max 출력을 위한 csv 파일 read
    experiment = value['experiment data']

    df = pd.read_csv(experiment)

    # 각각의 행 데이터를 list로 뽑아서 min, max 추출
    range_list = []
    for i in range(len(y_list)):
        # csv의 header y값 : y_value
        y_value = y_list[i]
        # header로 추출한 열 데이터 list로 변환 : convert_y_list
        convert_y_list = df[y_value].tolist()
        # 열 데이터에서 min, max 추출
        min_value = min(convert_y_list)
        max_value = max(convert_y_list)

        # min, max value type이 int인지 float인지 check 필요, float인 경우 소수점 3째자리에서 반올림 진행해야함
        if isinstance(min_value, int):
            pass
        else:
            min_value = round(min_value, 2)
        if isinstance(max_value, int):
            pass
        else:
            max_value = round(max_value, 2)
        range_dict = {}
        range_dict["min"] = min_value
        range_dict["max"] = max_value
        # range_list == [{'min': 2, 'max': 432}, {'min': 7.05, 'max': 14.55}]
        range_list.append(range_dict)

    # range_str_list == ['appearance', 'identification', 'dissolution test']
    range_str_list = []
    # result_range_list == [{'min': 2, 'max': 432}, {'min': 7.05, 'max': 14.55}]
    result_range_list = []
    # min, max range dict
    for z in range(len(y_list)):
        range_str_list.append(y_list[z])
        result_range_list.append(range_list[z])
        effect_range_dict = dict(zip(range_str_list, result_range_list))

    effect_list = []
    effect_range_dict = list(effect_range_dict.items())
    for f in range(len(y_list)):
        new_dict = {}
        y_str = "Y%d" % (f+1)
        new_dict[y_str] = {effect_range_dict[f][0] : effect_range_dict[f][1]}
        effect_list.append(new_dict)

    return contour_path_list, response_path_list, pareto_path_list, effect_list

def frame(value):
    print('frame 함수를 탐')
    res_dict={}
    code=""
    msg=""
    mkdir_path = mkdir(value)
    contour_path_list, response_path_list, pareto_path_list, effect_list = R_graph(value)
    res_dict["contour"] = contour_path_list
    res_dict["response"] = response_path_list
    res_dict["pareto"] = pareto_path_list
    res_dict["effects"] = effect_list
    code = "000"
    msg = "success"

    return(res_dict, code, msg)