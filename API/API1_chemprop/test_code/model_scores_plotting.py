import pandas as pd
import os
import seaborn as sns
import matplotlib.pyplot as plt

# 1. 점수 적힌 csv 파일 경로  - 2개 따로 불러오기.
'''
names = ['regression', 'classification']
# model_path = 'C:/Users/coco7/PycharmProjects/pharmai/practice/'
paths = {}
for name in names:
    # os.path.join(tm.model_path, f'checkpoint/{name}_model/test_scores.csv')
    path = os.path.join(model_path, f'checkpoint/{name}_model/test_scores.csv')
    paths[name] = path
print(paths)
'''

paths = {}
paths['regression']="C:/Users/coco7/PycharmProjects/pharmai/practice/checkpoint/reg_checkpoint/test_scores.csv"
paths['classification']="C:/Users/coco7/PycharmProjects/pharmai/practice/checkpoint/clf_checkpoint/test_scores.csv"

# 2-1. reg csv 파일 경로 -> df -> 그림 -> 저장
df_reg = pd.read_csv(paths['regression'])
sns.set_theme(style="whitegrid")
sns.barplot(data=df_reg, x=df_reg.columns[-1], y="Task",capsize=0.9)
plt.savefig('./socres_reg.png',bbox_inches='tight')
plt.show()

# 2-2. clf csv 파일 경로 -> df -> 그림 -> 저장
df_clf = pd.read_csv(paths['classification'])
sns.set_theme(style="whitegrid")
sns.barplot(data=df_clf, x=df_clf.columns[-1], y="Task")
plt.savefig('./socres_clf.png',bbox_inches='tight')
plt.show()
# => platform UI 상에서는 따로 따로 불러내서 그려내는 것이 맞는 것 같다.

'''
sns.set_theme(style="whitegrid")
fig, axes = plt.subplots(2,1)
sns.barplot(data=df_clf,x=df_clf.columns[-1],y='Task',ax=axes[0])
sns.barplot(data=df_reg,x=df_reg.columns[-1],y='Task',ax=axes[1])
axes[0].set_title('clf')
axes[1].set_title('reg')
plt.subplots_adjust(top=3)
plt.savefig('./socres.png',bbox_inches='tight')'''


# 4. clf 그려낸 것을 플랫폼 파라미터로

# 5. UI로 보내기.
# ------------------------
