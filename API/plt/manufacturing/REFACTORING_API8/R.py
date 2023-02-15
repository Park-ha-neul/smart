#-*- coding: utf-8 -*-
import subprocess
import pandas as pd
import csv
from csv import writer
import json
import logging

# 추론 테스트 시 input값을 인자로 받아야함
def runScript(value):
    input = value['cpp']
    # excipients 공백 사이에 .을 추가한 excipients를 넘겨줘야함. R에서는 공백 있을 시 error 발생
    for i in range(len(input)):
        origin_cpp = input[i]['factor']
        replace_cpp = origin_cpp.replace(" ", ".")
        input[i]['factor'] = replace_cpp

    # input -> csv 형태로 변환(excipients만 변환)
    with open('/data/aip/activate/manufacturing/api8/input_data.csv', 'w', newline="") as f:
        f.write("cpp, input.range.min, input.range.max" + "\n")
        for j in range(len(input)):
            dict=input[j]
            min = dict['input range']['min']
            max = dict['input range']['max']
            dict['input.range.min'] = min
            dict['input.range.max'] = max
            del(dict['input range'])
            w = csv.DictWriter(f, dict.keys())
            w.writerow(dict)
    # input -> csv 형태로 변환('개별 응답값에 대한 목표치')
    with open('/data/aip/activate/manufacturing/api8/target.csv', 'w', newline="") as f:
        # csv header로 cqas, min, max 생성
        f.write("cqas, min, max" + "\n")
        # input에서 response만 추출, response : [{'Y1': [{'Hardness': {'min': 9.5, 'max': 12.7}}], 'Y2': [{'Friability': {'min': 0, 'max': 0.15}}], 'Y3': [{'Disintegration ': {'min': 0, 'max': 10}}], 'Y4': [{'Dissolution': {'min': 90, 'max': 100}}]}]
        response = value['response']
        dict = response[0]
        result_dict = {}
        for keys in dict:
            cqas_dict = dict[keys][0]
            keyList = cqas_dict.keys()
            for item in keyList :
                result_dict['cqas'] = item
                result_dict['min'] = cqas_dict[item]['min']
                result_dict['max'] = cqas_dict[item]['max']
                w = csv.DictWriter(f, result_dict.keys())
                w.writerow(result_dict)

    # input(path) -> csv 형태로 변환(1. experiment_data_path, 2. design_path, 3. result_path)
    experiment_data_path = value['experiment data']
    design_path = value['design-path']
    result_path = value['result-path']

    with open('/data/aip/activate/manufacturing/api8/path_data.csv', 'w', newline="") as f:
        f.write("experiment_data_path, design_path, result_path" + "\n")
        dict = {}
        dict['experiment_data_path'] = experiment_data_path
        dict['design_path'] = design_path
        dict['result_path'] = result_path
        w = csv.DictWriter(f, dict.keys())
        w.writerow(dict)

    rlocation=["/usr/bin/Rscript", "/data/aip/activate/manufacturing/api8/result.R"]
    subprocess.run(rlocation)
    # R 스크립트 run을 시키면 생성되는 csv 파일(서버 경로와 local 경로 동잃하게 설정해줌) return
    design_output_path = pd.read_csv('/data/aip/activate/manufacturing/api8/design_output_path.csv', dtype=str)
    result_output_path = pd.read_csv('/data/aip/activate/manufacturing/api8/result_output_path.csv', dtype=str)

    return design_output_path, result_output_path