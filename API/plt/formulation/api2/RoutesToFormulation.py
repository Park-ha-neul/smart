import tensorflow as tf
import logging
import pandas as pd
# from collections import OrderedDict

def RoutesToFormulation(value,params):
    # try:
    # res_dict= OrderedDict()
    res_dict = {}
    response={}
    a=(list(params.values())[1])
    b=(list(params.values())[2])
    c=(list(params.values())[3])
    list1=[]
    list2=[]
    list3=[]
    list4=[]
    list5=[]
    list6=[]
    for z in a:
        list1.append(z[1])
    for xx in b:
        list2.append(xx[1])
    for vv in c:
        list3.append(vv[1])
    for zz in a:
        list4.append(zz[0])
    for xxx in b:
        list5.append(xxx[0])
    for vvv in c:
        list6.append(vvv[0])
    if 'routes' in value:
        key_routes = value['routes']
        if key_routes in params :
            if key_routes=='oral':
                res_dict['include']=list1
                msg="success"
                code="000"
                #include_list.extend(params[key_routes][1])
                #res_dict['include_list']=include_list
                #excipient_list.extend(params[key_routes])
                res_dict["excipient list"]=list4
                response["code"]=code
                response["msg"]=msg
                response["result"] = res_dict
            elif key_routes=='local':
                res_dict['include']=list2
                msg="success"
                code="000"
                #include_list.extend(params[key_routes][1])
                #res_dict['include_list']=include_list
                #excipient_list.extend(params[key_routes])Y
                res_dict["excipient list"]=list5
                response["code"]=code
                response["msg"]=msg
                response["result"] = res_dict
            elif key_routes=='parenteral':
                res_dict['include']=list3
                msg="success"
                code="000"
                #include_list.extend(params[key_routes][1])
                #res_dict['include_list']=include_list
                #excipient_list.extend(params[key_routes])
                res_dict["excipient list"]=list6
                response["code"]=code
                response["msg"]=msg
                response["result"] = res_dict
        else:
            code="002"
            msg="routes의 value값이 누락되었습니다. 다시 한번 확인해주세요. "


    else:
        code="001"
        msg="routes가 누락되었습니다."
    return(res_dict, code, msg)