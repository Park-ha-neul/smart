#-*- coding: utf-8 -*-
import subprocess
import pandas as pd
import csv
from csv import writer
import json

# 추론 테스트 시 input값을 인자로 받아야함
def runScript(value):
    input = value['cpp']
    # cpp 공백 사이에 .을 추가한 excipients를 넘겨줘야함. R에서는 공백 있을 시 error 발생
    for i in range(len(input)):
        origin_cpp = input[i]['factor']
        replace_cpp = origin_cpp.replace(" ", ".")
        input[i]['factor'] = replace_cpp

    # input -> csv 형태로 변환
    with open('/data/aip/activate/manufacturing/api6/input_data.csv', 'w', newline="") as f:
        f.write("cpp, input.range.min, input.range.max" + "\n")
        for j in range(len(input)):
            dict=input[j]
            print('dict : ', dict)
            min = dict['input range']['min']
            max = dict['input range']['max']
            dict['input.range.min'] = min
            dict['input.range.max'] = max
            del(dict['input range'])
            w = csv.DictWriter(f, dict.keys())
            w.writerow(dict)
    # local
    rlocation=["/usr/bin/Rscript", "/data/aip/activate/manufacturing/api6/DoE.R"]
    subprocess.run(rlocation)

    # R 스크립트 run을 시키면 생성되는 csv 파일(서버 경로와 local 경로 동잃하게 설정해줌) return
    r_csv = pd.read_csv('/data/aip/activate/manufacturing/api6/output_data.csv', dtype=str)

    return r_csv