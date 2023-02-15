#-*- coding: utf-8 -*-
import logging
import pandas as pd
import csv
import ast
import os
# R sciprt 불러오기위한 file import
# local
import R

# R로 돌린 결과값 csv를 가져온 후 변환
# inpt으로 받는 path가 서버에 존재하지 않는 경우 path를 만들어줘야함
def mkdir(value):
    design_path = value['design-path']
    result_path = value['result-path']
    logging.info('@@@@@@@@@@@@ success mkdir')
    if os.path.isdir(design_path) :
        pass
    else:
        os.makedirs(design_path)
    if os.path.isdir(result_path) :
        pass
    else:
        os.makedirs(result_path)
    logging.info('@@@@@@@@@@@@ end mkdir')

def R_graph(value):
    # rScript로 생성된 그래프의 경로를 output 형태에 맞게 변환하는 과정
    design_output_path, result_output_path = R.runScript(value)
    logging.info('@@@@@@@@@@@@ success runScript')

    design = design_output_path['ch_design']
    result = result_output_path['ch_result']

    design_path_list = []
    for i in range(len(design)):
        design_path_list.append(design[i])

    result_path_list = []
    for j in range(len(result)):
        result_path_list.append(result[j])

    final_dict = {}
    final_dict["design"] = design_path_list
    final_dict["contour"] = result_path_list

    return design_path_list, final_dict

def factor(value):
    # factor 추출(요인 변수 표 생성)
    logging.info('@@@@@@@@@@@@ start mkfactor')
    formulation = value['formulation']
    space_list = []
    excipient_list = []
    for i in range(len(formulation)):
        name_dict = {}
        x_str = "X%d" % (i+1)
        name = formulation[i]['excipients']
        min = formulation[i]['input.range.min']
        max = formulation[i]['input.range.max']

        #knowledge space
        k_range_dict = {}
        k_range_dict["min"] = min
        k_range_dict["max"] = max

        #design space(R에서 생성을 해야하지만 일단은 knowledge space - 1 값으로 고정해둔 상태)
        d_range_dict = {}
        d_min = float(min) - 0.1
        d_max = float(max) - 0.1
        d_range_dict["min"] = d_min
        d_range_dict["max"] = d_max

        #control space(design space의 70내 범위)
        c_range_dict = {}
        middle = (d_min + d_max)/2
        c_min = middle - (middle*0.35)
        c_max = middle + (middle * 0.35)
        if isinstance(c_min, int):
            pass
        else:
            c_min = round(c_min, 2)
        if isinstance(c_max, int):
            pass
        else:
            c_max = round(c_max, 2)
        c_range_dict["min"] = c_min
        c_range_dict["max"] = c_max

        space_dict = {}
        space_dict["knowledge space"] = k_range_dict
        space_dict["design space"] = d_range_dict
        space_dict["control space"] = c_range_dict

        name_dict[x_str] = {name : space_dict}
        space_list.append(name_dict)

        # excipient 추출(부형제 결과값 출력)
        excipient_dict = {}
        excipient_dict[name] = c_range_dict
        excipient_list.append(excipient_dict)

    return space_list, excipient_list

def frame(value):
    res_dict={}
    code=""
    msg=""
    mkdir_path = mkdir(value)
    design_path_list, final_dict = R_graph(value)
    space_list, excipient_list = factor(value)
    res_dict["design"] = design_path_list
    res_dict["final"] = final_dict
    res_dict["factor"] = space_list
    res_dict["excipient"] = excipient_list
    code = "000"
    msg = "success"

    return(res_dict, code, msg)