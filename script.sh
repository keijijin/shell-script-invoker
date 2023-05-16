#!/bin/bash

# 引数が2つでなければエラーメッセージを表示して終了
if [ $# -ne 2 ]; then
    echo "正確に2つの引数が必要です: 例 ./script.sh arg1 arg2"
    exit 1
fi

# 引数を変数に格納
arg1=$1
arg2=$2

# 引数を表示
echo "最初の引数: $arg1"
echo "二番目の引数: $arg2"
