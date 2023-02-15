import logging
import os
import warnings

import chemprop
import numpy as np
import pandas as pd
# from sklearn.metrics import confusion_matrix,classification_report
from sklearn.metrics import confusion_matrix, precision_score, recall_score, f1_score, accuracy_score

from config import get_pre_paths_dict, InferenceConfig

logging.basicConfig(level=logging.INFO)


# caco2, BA, bi basic, bi acidic 전용 성능 지표 만들기
def clf_test(data_path, tm):
    # 먼저 prediction 돌리기
    test_path = get_pre_paths_dict(tm)
    arguments = [
        '--preds_path', os.path.join(tm.train_data_path, 'preds.csv'),
        '--checkpoint_dir', os.path.join(tm.model_path, f'checkpoint/regression_model'),
        '--test_path', data_path,
        '--gpu', '0'
    ]

    args = chemprop.args.PredictArgs().parse_args(arguments)
    preds = chemprop.train.make_predictions(args=args)
    print(preds)

    # 성능 측정하기
    ## data
    y_true_df = pd.read_csv(data_path)
    y_pred_df = pd.read_csv(os.path.join(tm.train_data_path, 'preds.csv'))

    ## smiles 제거
    y_true = y_true_df.iloc[:, 1:]
    y_pred = y_pred_df.iloc[:, 1:]

    # classification columns 만 뽑기 (추후 caco2 도 적용)
    clf = InferenceConfig.classification_cols
    y_true = y_true[clf]
    y_pred = y_pred[clf]

    ## binary로 변환하기

    def df_to_binary(df):
        def to_binary(row, col):
            v = row[col]
            v = 1 if v > 0.5 else 0
            return v

        for col in df.columns:
            df[col] = df.apply(lambda row: to_binary(row, col), axis=1)

    df_to_binary(y_pred)
    df_to_binary(y_true)

    num_tasks = len(y_true.columns)

    valid_preds = [[] for _ in range(num_tasks)]
    valid_targets = [[] for _ in range(num_tasks)]
    for i in range(num_tasks):
        for j in range(len(y_pred)):
            if not np.isnan(y_true.iloc[j, i]):  # Skip those without targets
                valid_preds[i].append(y_pred.iloc[j, i])
                valid_targets[i].append(y_true.iloc[j, i])
    warnings.filterwarnings('ignore')

    for i in range(3):
        logging.info('-------------------------')
        logging.info(f'COLUMN : {y_true.columns[i]}')
        vp = valid_preds[i]
        vt = valid_targets[i]
        # print(classification_report(vt0, vp0)) - sklearn version 1.0 이상만 되고, 플랫폼 호환 문제로 실행하지 않음.
        cm = confusion_matrix(vt, vp)
        logging.info(f'confusion matrix : \n {cm}')
        logging.info('Precision: %.3f' % precision_score(vt, vp))
        logging.info('Recall: %.3f' % recall_score(vt, vp))
        logging.info('Accuracy: %.3f' % accuracy_score(vt, vp))
        logging.info('F1 Score: %.3f' % f1_score(vt, vp))
