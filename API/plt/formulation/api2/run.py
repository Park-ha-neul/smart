import tensorflow as tf
import logging
import pandas as pd
import csv
import ast
# from . import RoutesToFormulation as R2F
# from RoutesToFormulation import RoutesToFormulation as R2F
from RoutesToFormulation import *

tf.logging.set_verbosity(tf.logging.INFO)

def train(tm):
    pass

def init_svc(im):
    params = {"routes": [["routes_classification", "include"]], "oral": [["Capsule, Oral Capsule", "Capsule, Oral Capsule"], ["Tablet", "Tablet "], ["Oral Suspension, Suspension", "Oral Suspension, Suspension"], ["Oral Solution", "Oral solution, Elixir, Drop, Liquid, Syrup"], ["Granule", "Granule, Gum, Troche"], ["Powder", "Powder"], ["Gel", "Gel"], ["Sublingual", "Sublingual Tablet Subblingual Film, \nSubblingual Powder"], ["Sublingual Spray", "Subblingual Spray"], ["Bucal Tablet, Bucal Film", "Bucal Tablet & Bucal Film, Film"]], "local": [["Aerosol", "Injection"], ["Transmucosal Lozenge", "Intramuscular Injection"], ["Topical Suspension, Topical Solution", "Intravenous Injection"], ["Cream, Topical Cream", "Implant, Subcutaneous Implant, \nSubcutaneous injection"], ["Emusion", "Injection Suspension"], ["Gel, Topical Gel", "Aerosol, Powder"], ["Lotion", "Transmucosal Lozenge"], ["Ointment", "Topical Suspension, Topical Solution"], ["Patch", "Cream, Topical Cream"], ["Shampoo, Topical Shampoo", "Emusion"], ["Nasal Spray, Nasal Solution", "Gel, Paste"], ["Spray", "Lotion"], ["Ophthalmic Gel", "Ointment"], ["Ophthalmic Solution", "Patch"], ["Ophthalmic Suspension, Ophthalmic Emulsion", "Shampoo, Topical Shampoo"], ["Intravitreal Implant", "Nasal Spray, Nasal Solution"], ["Suppository", "Spray"], ["Suspension", "Ophthalmic gel"], ["Vaginal", "Ophthalmic solution"], ["Urethral Suppository", "Ophthalmic suspension, Ophthalmic Emulsion"]], "parenteral": [["Injection", "INTRAVITREAL IMPLANT"], ["Intramuscular injection", "Suppository"], ["Intravenous Injection", "Suspension"], ["Implant", "Vaginal Ring, Vaginal insert, \nVaginal gel, Vaginal Cream"], ["Injection Suspension", "URETHRAL SUPPOSITORY"]]}

    return params

def inference(df, params,batch_id):
    logging.info('params : %s', params)
    response = {}
    value=df.values[0,0]
    tuple = RoutesToFormulation(value, params)
    res_dict=tuple[0]
    code = tuple[1]
    msg = tuple[2]
    try:
        response["result"]=res_dict
        response["code"]=code
        response["msg"]=msg
    except:
        code = "999"
        msg = "정의되지 않은 error입니다."
        response["code"]=code
        response["msg"]=msg

    return response