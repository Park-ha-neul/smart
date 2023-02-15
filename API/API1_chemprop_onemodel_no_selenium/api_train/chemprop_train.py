# clf , reg  각각 함수로 만들어서 script하나에 만들어주기.
# return save_dir
import chemprop
from config import TrainConfig


def regression_train(name, data_path, tm, hpo_param_path):  # ens 1,2,3 / drugbank,scifinder
    train_args_basic = TrainConfig().get_regression_args(name, data_path, tm)
    if hpo_param_path is None:
        arguments = train_args_basic
    else:
        arguments = train_args_basic + ['--config_path', hpo_param_path]

    args = chemprop.args.TrainArgs().parse_args(arguments)
    # loads data, initializes the model, and runs training, validation, and testing of the model.
    # chemprop.train.run_training(args=args) parameter data가 있어야 함??
    chemprop.train.cross_validate(args=args, train_func=chemprop.train.run_training)


def classification_train(name, data_path, tm):
    arguments = TrainConfig().get_classification_args(name, data_path, tm)  # 0405 인자 채워주기

    args = chemprop.args.TrainArgs().parse_args(arguments)
    # loads data, initializes the model, and runs training, validation, and testing of the model.
    # chemprop.train.run_training(args=args)
    chemprop.train.cross_validate(args=args, train_func=chemprop.train.run_training)
