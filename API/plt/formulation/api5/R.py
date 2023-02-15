#-*- coding: utf-8 -*-
import subprocess
import pandas as pd
import csv
from csv import writer
import json

# 추론 테스트 시 input값을 인자로 받아야함
def runScript(value):
    input = value['formulation']
    # excipients 공백 사이에 .을 추가한 excipients를 넘겨줘야함. R에서는 공백 있을 시 error 발생
    for i in range(len(input)):
        origin_excipients = input[i]['excipients']
        replace_excipients = origin_excipients.replace(" ", ".")
        input[i]['excipients'] = replace_excipients

    # input -> csv 형태로 변환
    with open('/data/aip/activate/api5/input_data.csv', 'w', newline="") as f:
        f.write("excipients, input.range.min, input.range.max" + "\n")
        for j in range(len(input)):
            dict=input[j]
            # 필요없는 행 지워버리기
            del(dict['kind'])
            del(dict['max'])
            del(dict['use range'])
            min = dict['input range']['min']
            max = dict['input range']['max']
            dict['input.range.min'] = min
            dict['input.range.max'] = max
            del(dict['input range'])
            w = csv.DictWriter(f, dict.keys())
            w.writerow(dict)
    # 플랫폼
    rlocation=["/usr/bin/Rscript", "/data/aip/activate/api5/DoE.R"]
    subprocess.run(rlocation)
    # output = subprocess.run(rlocation, capture_output=True)

    # R 스크립트 run을 시키면 생성되는 csv 파일(서버 경로와 local 경로 동잃하게 설정해줌) return
    r_csv = pd.read_csv('/data/aip/activate/api5/output_data.csv', dtype=str)

    return r_csv