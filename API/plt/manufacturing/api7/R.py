#-*- coding: utf-8 -*-
import subprocess
import pandas as pd
import csv
from csv import writer
import json
import logging

# 추론 테스트 시 input값을 인자로 받아야함
def runScript(value):
    logging.info('runScript start')
    input = value['cpp']
    # excipients 공백 사이에 .을 추가한 excipients를 넘겨줘야함. R에서는 공백 있을 시 error 발생
    for i in range(len(input)):
        origin_cpp = input[i]['factor']
        replace_cpp = origin_cpp.replace(" ", ".")
        input[i]['factor'] = replace_cpp

    # input -> csv 형태로 변환(excipients만 변환)
    with open('/data/aip/activate/manufacturing/api7/input_data.csv', 'w', newline="") as f:
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

    # input(path) -> csv 형태로 변환 필요 (1. experiment-data path, 2. pareto-path, 3. contour-path, 4. response-path)
    experiment_data_path = value['experiment data']
    pareto_path = value['pareto-path']
    contour_path = value['contour-path']
    response_path = value['response-path']

    with open('/data/aip/activate/manufacturing/api7/path_data.csv', 'w', newline="") as f:
        f.write("experiment_data_path, pareto_path, contour_path, response_path" + "\n")
        dict = {}
        dict['experiment_data_path'] = experiment_data_path
        dict['pareto_path'] = pareto_path
        dict['contour_path'] = contour_path
        dict['response_path'] = response_path
        w = csv.DictWriter(f, dict.keys())
        w.writerow(dict)

    rlocation=["/usr/bin/Rscript", "/data/aip/activate/manufacturing/api7/graph.R"]
    subprocess.run(rlocation)

    # R 스크립트 run을 시키면 생성되는 csv 파일(서버 경로와 local 경로 동잃하게 설정해줌) return
    output_path = pd.read_csv('/data/aip/activate/manufacturing/api7/output_data.csv', dtype=str)
    output_path2 = pd.read_csv('/data/aip/activate/manufacturing/api7/output_data2.csv', dtype=str)
    y_list = value['header']

    return output_path, output_path2, y_list