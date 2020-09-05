import pandas as pd
import os
import re

pat = re.compile("\d+.*"+'.txt')

path = '/Users/tithighosh/Library/Application Support/AirDroid/Downloads/article_data 2'
data=pd.read_csv('data_pro.csv', index_col=0)

for lab, row in data.iterrows():
    
    if not str(data.loc[lab, 'Text'])=='nan':
        continue
    
    file = data.loc[lab, 'filename'][84:]
     
    with open(os.path.join(path, file)) as f:
        content = f.read()
        
    data.loc[lab, 'Text'] = content

data.to_csv('fake_news_dataset.csv')
     
