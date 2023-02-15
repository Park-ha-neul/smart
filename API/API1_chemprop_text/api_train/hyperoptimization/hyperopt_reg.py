import os

import chemprop

from config import HyperoptConfig


def hyp(name, data_path):
    """

    :param name: regression or classification
    :param data_path: csv file
    :return: path of hyperopt args json file
    hyperopt args    : depth, dropout, ffn_num_layers, hidden_size
    - depth          : The number of message passing iterations is selected from {2, 3, 4, 5, 6}
    - dropout        : The dropout probability is selected from {0.0, 0.05, …, 0.4}
    - ffn_num_layers : The number of feed-forward layers after message passing is selected from {1, 2, 3}
    - hidden_size    : The hidden size of the neural network layers is selected from {300, 400, …, 2400}

    ex)
    {
    "depth": 5,
    "dropout": 0.0,
    "ffn_num_layers": 3,
    "hidden_size": 1000
}
    """
    hyp_json_path = os.path.join(HyperoptConfig.hyp_path, f'hyperopt_{name}.json')
    args_path = [
        '--data_path', data_path,
        '--config_save_path', hyp_json_path,
    ]

    arguments = args_path + HyperoptConfig.hyp_args

    args = chemprop.args.HyperoptArgs().parse_args(arguments)
    chemprop.hyperparameter_optimization.hyperopt(args=args)
    return hyp_json_path

