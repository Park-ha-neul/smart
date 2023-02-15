import os

import numpy as np
import pandas as pd

# from ..config import PreConfig
from config import PreConfig, get_paths_dict  # 플랫폼 내부에선 이렇게 받아들일까?


# 컬럼 명들 상수값으로 config에 넣어주기
# 안에 verbose 지워주기

# SCIFINDER
# pka
'''def preprocessing_pka(df):
    # 값 존재유무 0,1로 따로 컬럼 넣기
    def binary_df(df):
        pkab = PreConfig.pkab
        bidfs = df[pkab]
        bidfs = bidfs.fillna(pd.NA)

        def binary(row, col):
            v = row[col]
            v = 0 if v is pd.NA else 1
            return v

        for col in pkab:
            bidfs[col] = bidfs.apply(lambda row: binary(row, col), axis=1)
        return bidfs

    bidf = binary_df(df)

    map_ = {'Bi Basic': 'Most Basic', 'Bi Acidic': 'Most Acidic'}
    for key, value in map_.items():
        df[key] = bidf[value]
    return df

'''
# pH Mass Solubility
def preprocessing_ph_mass_solubility(df):
    def phms_log(row, col):
        v = row[col]
        # log 취하기
        try:
            v = np.log10(v)
            if v == float('-inf'): return np.nan
            return v
        except:
            return v

    ms = PreConfig.ms
    for col in ms:
        df[col] = df.apply(lambda row: phms_log(row, col), axis=1)

    return df


# DRUGBANK
# water solubility
def preprocessing_water_solubility(df):
    ws = PreConfig.ws
    # log 취해주기
    df[ws] = np.log10(df[ws])
    return df


# melting point
def preprocessing_melting_point(df):
    mp = PreConfig.mp

    def log_mp(row, col):
        v = row[col]
        try:
            v = np.log10(float(v))
            if v == float('-inf'):
                v = np.nan
            return v
        except:
            return np.nan

    df[mp] = df.apply(lambda row: log_mp(row, mp), axis=1)
    return df


# boiling point
def preprocessing_boiling_point(df):
    bp = PreConfig.bp

    def log_bp(row, col):
        v = row[col]
        try:
            v = np.log10(float(v))
            if v == float('-inf'):
                v = np.nan
            return v
        except:
            return np.nan

    df[bp] = df.apply(lambda row: log_bp(row, bp), axis=1)

    return df


# dataset 나누기
def split_data(df, tm):
    """
    전처리 완료한 데이터 프레임을 tm.train_data_path + data.csv 형태로 저장한 후에 저장경로를 딕셔너리로 반환한다.
    :param df: 전처리 완료한 데이터프레임
    :param tm: tm.train_data_path : 전처리 완료된 데이터 경로
    :return: 전처리 완료한 데이터 경로
    """
    # 모드별로 물성 컬럼을 나눈다.
    classification_df = df[PreConfig.classification_cols]
    regression_df = df[PreConfig.regression_cols]

    # 전처리한 데이터를 저장할 경로를 가져온다.
    preprocessed_paths_dict = get_paths_dict(tm)
    # 각 데이터프레임을 csv로 지정한 경로에 저장한다.
    classification_df.to_csv(preprocessed_paths_dict['classification'], encoding='utf-8', index=False)
    regression_df.to_csv(preprocessed_paths_dict['regression'], encoding='utf-8', index=False)

    return preprocessed_paths_dict


def preprocess(tm):
    """

    :return:

    """
    data_path = tm.train_data_path
    for i in os.listdir(data_path):
        if '_data_.csv' not in i :
            # 플랫폼에서 어떤 이름으로 데이터셋을 올려도 전처리 할 수 있도록 함.
            # 플랫폼에서 전처리 한 후 보여지는 데이터 명인 _0_data_.csv 형태로 나오는 데이터 제외
            data_file = i
            df1 = pd.read_csv(os.path.join(data_path, data_file), engine='python')
            df2 = preprocessing_ph_mass_solubility(df1)
            df3 = preprocessing_water_solubility(df2)
            df4 = preprocessing_melting_point(df3)
            df5 = preprocessing_boiling_point(df4)
    # split_data
    # regression,classification 별
    # dataset 디렉토리에  csv 형태로 떨어짐.
            return split_data(df5, tm)
