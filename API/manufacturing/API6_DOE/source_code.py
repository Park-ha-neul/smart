#-*- coding: utf-8 -*-
import logging
import pandas as pd
import csv
import ast
# R sciprt 불러오기위한 file import
# local
from manufacturing.API6_DOE.R import runScript
# 플랫폼
# import R -> 밑에 R.runScript로 바꿔야함

# R로 돌린 결과값 csv를 가져온 후 변환
# inpt을 받아야함
def header(value):
    cpp_header = []
    cqa_header = []
    header = []
    cpp = value['cpp']
    cqas = value['CQAs']
    for i in range(len(cpp)):
        export_dict = cpp[i]
        cpps = export_dict.get("factor")
        cpp_header.append(cpps)
    for j in range(len(cqas)):
        cqa = cqas[j]
        cqa_header.append(cqa)
    header = cpp_header + cqa_header

    return (cpp_header, header)

def experiment_data(value):
    # excipients_header 부분만 필요함, cqas의 header는 필요하지 않음
    cpp_header, header_result = header(value)
    r_csv = runScript(value)
    experiment_dict = {}
    experiment_list = []
    experiment_result = []
    for i in range(len(r_csv)):
        for j in range(len(cpp_header)):
            cpps = cpp_header[j]
            blank_remove_cpps = cpps.replace(" ", ".")
            experiment_data = r_csv.loc[i][blank_remove_cpps]
            experiment_dict[cpps] = experiment_data
        experiment_list.append(experiment_dict.copy())
    print('experiment_list : ', experiment_list)
    return experiment_list

def frame(value):
    res_dict={}
    code=""
    msg=""
    print('frame 함수를 탔음')
    excipients_header, header_result = header(value)
    experiment_result = experiment_data(value)
    res_dict["header"] = header_result
    res_dict["experiment data"] = experiment_result
    code = "000"
    msg = "success"

    return(res_dict, code, msg)