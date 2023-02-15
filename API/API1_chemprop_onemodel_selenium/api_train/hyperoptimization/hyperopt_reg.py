import chemprop
import os
import sys

from config import HyperoptConfig


def hyp(name, data_path):
    hyp_json_path = os.path.join(HyperoptConfig.hyp_path, f'hyperopt_{name}.json')
    args_path = [
        '--data_path', data_path,
        '--config_save_path', hyp_json_path,
    ]

    arguments = args_path + HyperoptConfig.hyp_args

    args = chemprop.args.HyperoptArgs().parse_args(arguments)
    chemprop.hyperparameter_optimization.hyperopt(args=args)
    return hyp_json_path

