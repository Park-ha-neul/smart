# 추론 1차
# model path를 init_svc에서 paarams['model_path']로 받아와야 한다.
# 2차로는 caco2, melting point, boiling point, 전체적인 모델 성능 향상 후 변경할 것임.
import chemprop
import time
from config import InferenceConfig, errors
import os
import logging
logging.basicConfig(level=logging.INFO)

# smiles = ['CCC']
# smiles = ['CSc1ccc2Sc3ccccc3N(CCC4CCCCN4C)c2c1']
# smiles=[['CCC'], ['CCCC'], ['OCC']]
# smiles=['[O](|[Sn](c1ccccc1)(c2ccccc2)c3ccccc3)C(=O)CC#N'] # invalid SMILES
# ------------------------------------------------------------------------------------------------------------------
class PredictArgsLoadModel:
    # arguments 설정

    def __init__(self, im):
        """
            :return: pred_dict = {'res': res_round, 'dsg': dsg, 'cnt': cnt}
            """
        params: dict
        self.im = im  # 이걸 선언해줘야 밑에서 im.model_path를 가져올 수 있는 것이 아닌가?

        basic_arguments = [
            '--test_path', '/dev/null',
            '--preds_path', '/dev/null',
        ]
        # 1. regression arguments
        regression_arguments = basic_arguments \
                               + ['--checkpoint_dir', os.path.join(im.model_path, f'checkpoint/regression_model')]

        # 3. arguments parsing, model_objects
        self.regression_args = chemprop.args.PredictArgs().parse_args(regression_arguments)
        self.regression_model_objects = chemprop.train.load_model(args=self.regression_args)

    def get_model(self):
        # return self.regression_model_objects, self.classification_model_objects
        return self.regression_model_objects

    def get_args(self):
        # return self.regression_args, self.classification_args
        return self.regression_args


# ---------------------------------------------------------------------------------------------------
def predict(smiles, params):
    # 추론 실행
    start = time.time()
    args_model = params['args_model']  # init_svc할 때 가져옴
    regression_model = args_model.get_model()
    regression_args = args_model.get_args()
    try:
        regression_predicted = chemprop.train.make_predictions(args=regression_args,
                                                               smiles=smiles,
                                                               model_objects=regression_model
                                                               )

        # dictionary로 리턴하게 해서 바꿔보기.
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

        # dictionary로 만들기 -> 인자 참조.
        predicted_regression_dict = cols_value_dict(regression_predicted, InferenceConfig.regression_cols)

        end = time.time()


        print(f'Total prediction time is {end - start} sec.')  # 평균 3초

        return predicted_regression_dict
    except SyntaxError:
        errors('005')
        logging.debug('Invalid SMILES. Please input the right SMILES.')


