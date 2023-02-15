# 추론 1차
# model path를 init_svc에서 paarams['model_path']로 받아와야 한다.
# 2차로는 caco2, melting point, boiling point, 전체적인 모델 성능 향상 후 변경할 것임.
import time

import chemprop
import torch

from config import InferenceConfig


def cols_value_dict(predicted, cols):
    """
    각각 예측한 값과 물성 이름을 같이 엮어서 딕셔너리로 만든다.
    :param predicted:예측해서 나온 값의 리스트 [[값1,값2,값3...]]
    :param cols: [물성1,물성2,물성3..]
    :return: {물성1:값1, 물성2:값2, 물성3:값3...}
    """
    values_list = predicted[0]  # 리스트 안에 리스트가 있으므로 그 안에 리스트를 추출한다.
    pred_dict = {prop_col: value for prop_col, value in zip(cols, values_list)}
    return pred_dict


# ---------------------------------------------------------------------------------------------------
def predict(smiles, params, log):
    # 추론 실행
    start = time.time()
    args_model = params['args_model']  # init_svc할 때 가져옴
    regression_model = args_model.get_model()
    regression_args = args_model.get_args()
    log.info(f'===get model, args {time.time() - start}')

    regression_predicted = chemprop.train.make_predictions(args=regression_args,
                                                           smiles=smiles,
                                                           model_objects=regression_model
                                                           )
    log.info(f'---make prediction {time.time() - start}')
    # dictionary로 리턴하게 해서 바꿔보기.

    # dictionary로 만들기 -> 인자 참조.

    predicted_regression_dict = cols_value_dict(regression_predicted, InferenceConfig.regression_cols)
    log.info(f'-----predicted dict {time.time() - start}')
    end = time.time()

    able_to_use_gpu = torch.cuda.is_available()
    log.info(f'----ABLE TO USE GPU? {able_to_use_gpu}')
    log.info(f'Total prediction time is {end - start} sec.')  # 평균 3초
    return predicted_regression_dict
