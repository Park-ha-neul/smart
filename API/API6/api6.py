import tensorflow as tf
import logging
import os
from PIL import Image

# input으로 받은 path가 존재하지 않으면 디렉토리 생성 후 거기다가 저장
def mkdir(input_path):
    input_path = input_path
    logging.info('input_path : %s', input_path)
    if os.path.isdir(input_path):
        return input_path
    else:
        os.makedirs(input_path)
        return input_path

def api6(value):
    res_dict={}
    paths={}
    pngs={}
    DoE={}
    contour_dict={}
    design_dict={}
    pareto_dict={}
    response_dict={}
    result_dict={}
    dict_from_csv = {}
    input_type = ""
    sdf_path = ""
    ##################################################################start key error exception
    if 'experiment data' in value:
        pass
    else:
        res_dict = "{}"
        code="001"
        msg="experiment data가 누락되었습니다."
        return(res_dict, code, msg)
    if 'parento-path' in value:
        pass
    else:
        res_dict = "{}"
        code="001"
        msg="parento-path가 누락되었습니다."
        return(res_dict, code, msg)
    if 'contour-path' in value:
        pass
    else:
        res_dict = "{}"
        code="001"
        msg="contour-path가 누락되었습니다."
        return(res_dict, code, msg)
    if 'response-path' in value:
        pass
    else:
        res_dict = "{}"
        code="001"
        msg="response-path가 누락되었습니다."
        return(res_dict, code, msg)
    if 'design-path' in value:
        pass
    else:
        res_dict = "{}"
        code="001"
        msg="design-path가 누락되었습니다."
        return(res_dict, code, msg)
    if 'result-path' in value:
        pass
    else:
        res_dict = "{}"
        code="001"
        msg="result-path가 누락되었습니다."
        return(res_dict, code, msg)
    ##################################################################end key error exception

    ##################################################################start value error exception
    if value['experiment data'] == '':
        res_dict="{}"
        code="002"
        msg="experiment data의 value가 누락되었습니다."
        return(res_dict, code, msg)
    elif value['parento-path'] == '':
        res_dict="{}"
        code="002"
        msg="parento-path의 value가 누락되었습니다."
        return(res_dict, code, msg)
    elif value['contour-path'] == '':
        res_dict="{}"
        code="002"
        msg="contour-path의 value가 누락되었습니다."
        return(res_dict, code, msg)
    elif value['response-path'] == '':
        res_dict="{}"
        code="002"
        msg="response-path의 value가 누락되었습니다."
        return(res_dict, code, msg)
    elif value['design-path'] == '':
        res_dict="{}"
        code="002"
        msg="design-path의 value가 누락되었습니다."
        return(res_dict, code, msg)
    elif value['result-path'] == '':
        res_dict="{}"
        code="002"
        msg="result의 value가 누락되었습니다."
        return(res_dict, code, msg)
    else:
        pass
    ##################################################################end value error exception

    if 'experiment data' in value and 'parento-path' in value and 'contour-path' in value and 'response-path' in value and 'design-path' in value and 'result-path' in value:
        parento = value['parento-path']
        logging.info('parento : %s', parento)
        contour = value['contour-path']
        response = value['response-path']
        design = value['design-path']
        result = value['result-path']

        #########################################################################start path form error
        if parento.endswith('/') and contour.endswith('/') and response.endswith('/') and design.endswith('/') and result.endswith('/'):
            pass
        else:
            res_dict="{}"
            code = "007"
            msg = "path 형식의 마지막은 '/'로 끝나야 합니다. 다시 한번 확인해주세요."
            return(res_dict, code, msg)

        #input으로 받은 path가 존재하지 않으면 생성
        parento = mkdir(parento)
        logging.info('parento : %s', parento)
        contour = mkdir(contour)
        response = mkdir(response)
        design = mkdir(design)
        result = mkdir(result)

        # parento
        # get png
        get_parento = Image.open('/data/aip/api/plot/parento.png')
        # 원래있던 png를 input으로 받은 path에 다른 이름으로 저장
        get_parento.save(parento + '/parento3.png')
        parento_path = parento + '/parento3.png'

        # contour
        # get png
        get_contour = Image.open('/data/aip/api/plot/contour.png')
        # 원래있던 png를 input으로 받은 path에 다른 이름으로 저장
        get_contour.save(contour + '/contour.png')
        contour_path = contour + '/contour.png'

        # response
        # get png
        get_response = Image.open('/data/aip/api/plot/response.png')
        # 원래있던 png를 input으로 받은 path에 다른 이름으로 저장
        get_response.save(response + '/response.png')
        response_path = response + '/response.png'

        # design
        # get png
        get_design = Image.open('/data/aip/api/plot/design.png')
        # 원래있던 png를 input으로 받은 path에 다른 이름으로 저장
        get_design.save(design + '/design.png')
        design_path = design + '/design.png'

        # result
        # get png
        get_result = Image.open('/data/aip/api/plot/result.png')
        # 원래있던 png를 input으로 받은 path에 다른 이름으로 저장
        get_result.save(result + '/result.png')
        result_path = result + '/result.png'

        contour_dict["path"]=parento
        design_dict["path"]=design
        pareto_dict["path"]=parento
        response_dict["path"]=response
        result_dict["path"]=result

        paths["contour plots"]=contour_dict
        paths["design space"]=design_dict
        paths["pareto chart and residual plot"]=pareto_dict
        paths["response surface plots"]=response_dict
        paths["result"]=result_dict

        contour_dict={}
        design_dict={}
        pareto_dict={}
        response_dict={}
        result_dict={}

        contour_dict["png"]=["contour1.png", "contour2.png", "contour3.png"]
        design_dict["png"]=["design1.png", "design2.png", "design3.png"]
        pareto_dict["png"]=["parento1.png", "parento2.png", "parento3.png"]
        response_dict["png"]=["response1.png", "response2.png", "response3.png"]
        result_dict["png"]=["result1.png", "result2.png", "result3.png"]

        pngs["contour plots"]=contour_dict
        pngs["design space"]=design_dict
        pngs["pareto chart and residual plot"]=pareto_dict
        pngs["response surface plots"]=response_dict
        pngs["result"]=result_dict

        DoE["paths"]=paths
        DoE["pngs"]=pngs

        res_dict["DoE"]=DoE
        code = "000"
        msg = "success"
    else:
        pass


    return(res_dict, code, msg)
